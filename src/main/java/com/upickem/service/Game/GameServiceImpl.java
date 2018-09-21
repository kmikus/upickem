package com.upickem.service.Game;

import com.upickem.model.Game;
import com.upickem.model.Quarter;
import com.upickem.model.SeasonType;
import com.upickem.model.Team;
import com.upickem.repository.GameRespository;
import com.upickem.schedule.ScheduleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameRespository gameRespository;

    @Value("${nfl.season.endMonth}")
    Long nflEndMonth;

    @Value("${nfl.season.endWeek}")
    Long nflEndWeek;

    @Value("${nfl.preseason.endWeek}")
    Long preseasonEndWeek;

    @Value("${nfl.regseason.endWeek}")
    Long regseasonEndWeek;

    @Value("${nfl.postseason.skipWeek}")
    Long skipWeek;

    @Override
    public List<Game> saveGames(List<Game> games) {
        return gameRespository.save(games);
    }

    @Override
    public List<LocalDate> getDatesOfGamesForWeekFromRemote(Year seasonYear, Long nflWeek, SeasonType seasonType) {
        List<LocalDate> dates = new ArrayList<>();
        String url = constructUrl(seasonYear, nflWeek, seasonType);
        Document doc;
        try {
            doc = buildDocumentFromRemote(url);
        } catch (Exception e) {
            log.error("Could not fetch game data from server for year: " + seasonYear.toString() +
                    " and week: " + nflWeek + " and season type: " + seasonType, e);
            return dates;
        }

        NodeList nodeList = doc.getElementsByTagName("g");
        for (int i = 0; i < nodeList.getLength(); i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            LocalDateTime dateTime = constructDateTimeFromXml(namedNodeMap);
            LocalDate date = dateTime.toLocalDate();
            dates.add(date);
        }

        return dates;
    }

    @Override
    public Long saveScheduleForYear(Year year) {
        Long countOfRecordsSaved = 0L;
        try {
            for (long i = 1; i <= preseasonEndWeek; i++) {
                countOfRecordsSaved += saveGames(pullGameDataFromRemoteServer(year, i, SeasonType.PRE)).size();
            }
            for (long i = 1; i <= regseasonEndWeek; i++) {
                countOfRecordsSaved += saveGames(pullGameDataFromRemoteServer(year, i, SeasonType.REG)).size();
            }
            for (long i = regseasonEndWeek + 1; i <= nflEndWeek; i++) {
                if (i == skipWeek) {
                    continue;
                }
                countOfRecordsSaved += saveGames(pullGameDataFromRemoteServer(year, i, SeasonType.POST)).size();
            }
            return countOfRecordsSaved;
        } catch (Exception e) {
            log.error("Unknown error occurred while trying to save schedule for year", e);
            return countOfRecordsSaved;
        }
    }

    @Override
    public List<Game> findGamesByYear(Year year) {
        return gameRespository.findGamesByYear(year);
    }

    @Override
    public List<Game> getGamesForCurrentWeek() {
        return gameRespository.findGamesByYearAndWeekAndSeasonTypeOrderByDateAndTime(getNflSeasonFromCurrentCalenderMonth(),
                ScheduleManager.getCurrentWeek(), ScheduleManager.getCurrentSeasonType());
    }

    public Year getNflSeasonFromCurrentCalenderMonth() {
        if (LocalDate.now().getMonthValue() <= nflEndMonth) {
            return Year.parse(String.valueOf(Year.now().getValue() - 1));
        } else {
            return Year.now();
        }
    }

    /*
    Game data is pulled from http://www.nfl.com/ajax/scorestrip
    where you may specify parameters in the URL string to get specific weeks such as
    http://www.nfl.com/ajax/scorestrip?season=2018&seasonType=PRE&week=3

    This will return an XML response such as the one below.
    Here's what we know about the attributes for the tags (Note and question marks are educated guesses:
    <ss> - Root
        <gms - Games: holds child game elements
        gd="0" - ???
        w="22" - Week: The week number. 1-5 for Preseason and 1-22 for Regular and Postseason (22 is the Super Bowl)
        y="2017" - Year
        t="P"> - Type?: P stands for Preseason and Postseason, R for Regular season
            <g - Game: individual game node
            eid="2018020400" - Id: Based on date and game number for that day?
            gsis="57500" - ??? Maybe some other type of id, also looks unique
            d="Sun" - Day of Week
            t="6:30" - Time
            q="F" - Quarter?: P? for pre (before game starts), F for final, 1-4 for num
            k="" - ???
            h="NE" - Home team
            hnn="patriots" - Home team full name
            hs="33" - Home score
            v="PHI" - Versus or away team
            vnn="eagles" - Versus or away team full name
            vs="41" - Versus or away score
            p="" - ???
            rz="" - Redzone?
            ga="" - ???
            gt="SB"/> - Game Type: PRE, REG, WC (Wildcard), DIV (Divisional), CON (Conference), SB (You know)
        </gms>
    </ss>

    // TODO
    We can also get live data from
    http://www.nfl.com/liveupdate/game-center/<eid>/<eid>_gtd.json
    where eid is from the schedule above
     */
    @Override
    public List<Game> pullGameDataFromRemoteServer(Year seasonYear, Long nflWeek, SeasonType seasonType) {

        List<Game> games = new ArrayList<>();
        String url = constructUrl(seasonYear, nflWeek, seasonType);
        Document doc;
        try {
            doc = buildDocumentFromRemote(url);
        } catch (Exception e) {
            log.error("Could not fetch game data from server for year: " + seasonYear.toString() +
                    " and week: " + nflWeek + " and season type: " + seasonType, e);
            return games;
        }
        NodeList nodeList = doc.getElementsByTagName("g");
        if (nodeList.getLength() == 0) {
            return games;
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            Long gameId = Long.parseLong(namedNodeMap.getNamedItem("eid").getNodeValue());
            Optional<Game> gameFromDb = gameRespository.findByGameId(gameId);
            if (gameFromDb.isPresent()) {
                Game game = gameFromDb.get();
                game = updateScoresOfGameObject(game, namedNodeMap);
                game = updateQuarterOfGameObject(game, namedNodeMap);
                games.add(game);
            } else {
                Game game = new Game();
                game.setGameId(gameId);
                game.setDateAndTime(Timestamp.valueOf(constructDateTimeFromXml(namedNodeMap)));
                game.setYear(seasonYear);
                game.setWeek(nflWeek);
                game.setSeasonType(seasonType);
                game.setHomeTeam(Team.valueOf(namedNodeMap.getNamedItem("h").getNodeValue()));
                game.setAwayTeam(Team.valueOf(namedNodeMap.getNamedItem("v").getNodeValue()));
                game = updateScoresOfGameObject(game, namedNodeMap);
                game = updateQuarterOfGameObject(game, namedNodeMap);
                games.add(game);
            }
        }

        for (int i=1; i<games.size(); i++) {
            if (games.get(i).getDateAndTime().toLocalDateTime().getDayOfWeek() !=
                    games.get(i-1).getDateAndTime().toLocalDateTime().getDayOfWeek()) {
                continue;
                // Case where the second hour is less than the first, i.e. the first game is a morning game and
                // the second game is an afternoon game
            } else if (games.get(i).getDateAndTime().toLocalDateTime().getHour() <
                    games.get(i-1).getDateAndTime().toLocalDateTime().getHour()) {
                LocalDateTime currentDateTime = games.get(i-1).getDateAndTime().toLocalDateTime();
                LocalDateTime fixedDateTime = currentDateTime.minus(12L, ChronoUnit.HOURS);
                games.get(i-1).setDateAndTime(Timestamp.valueOf(fixedDateTime));
            }
        }

        return games;
    }

    private LocalDateTime constructDateTimeFromXml(NamedNodeMap namedNodeMap) {
        String dateString = namedNodeMap.getNamedItem("eid").getNodeValue();
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(4, 6));
        int day = Integer.parseInt(dateString.substring(6, 8));

        String[] timeString = namedNodeMap.getNamedItem("t").getNodeValue().split(":");
        int hours = Integer.parseInt(timeString[0]);
        hours += 12;
        hours = hours % 24;
        int mins = Integer.parseInt(timeString[1]);
        return LocalDateTime.of(year, month, day, hours, mins);
    }

    private Game updateScoresOfGameObject(Game game, NamedNodeMap namedNodeMap) {

        String homeScore = namedNodeMap.getNamedItem("hs").getNodeValue();
        if (!homeScore.equals("")) {
            game.setHomeScore(Long.parseLong(homeScore));
        }

        String awayScore = namedNodeMap.getNamedItem("vs").getNodeValue();
        if (!awayScore.equals("")) {
            game.setAwayScore(Long.parseLong(awayScore));
        }

        if (namedNodeMap.getNamedItem("q").getNodeValue().equals("F")) {
            if (game.getHomeScore() > game.getAwayScore()) {
                game.setWinner(game.getHomeTeam());
            } else if (game.getAwayScore() > game.getHomeScore()) {
                game.setWinner(game.getAwayTeam());
            } else {
                game.setTie(true);
            }
        }

        return game;
    }

    private Game updateQuarterOfGameObject(Game game, NamedNodeMap namedNodeMap) {
        String quarterString = namedNodeMap.getNamedItem("q").getNodeValue();
        if (quarterString.equalsIgnoreCase("F")) {
            game.setQuarter(Quarter.FINAL);
        } else if (quarterString.equalsIgnoreCase("1")) {
            game.setQuarter(Quarter.ONE);
        } else if (quarterString.equalsIgnoreCase("2")) {
            game.setQuarter(Quarter.TWO);
        } else if (quarterString.equalsIgnoreCase("3")) {
            game.setQuarter(Quarter.THREE);
        } else if (quarterString.equalsIgnoreCase("4")) {
            game.setQuarter(Quarter.FOUR);
        } else if (quarterString.equalsIgnoreCase("OT")) { // TODO are we sure about this?
            game.setQuarter(Quarter.OT);
        }
        return game;
    }

    private Document buildDocumentFromRemote(String url) throws
            ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());
        return doc;
    }

    private String constructUrl(Year year, Long nflWeek, SeasonType seasonType) {
        if (year.isAfter(Year.now()) || year.isBefore(Year.parse("2000"))) {
            year = LocalDate.now().getMonthValue() > nflEndMonth ? Year.now() :
                    Year.parse(String.valueOf(Year.now().getValue() - 1));
            nflWeek = 1L;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("http://www.nfl.com/ajax/scorestrip?");
        sb.append("season=" + year.toString());
        sb.append("&seasonType=" + seasonType.toString());
        sb.append("&week=" + nflWeek);

        return sb.toString();
    }
}

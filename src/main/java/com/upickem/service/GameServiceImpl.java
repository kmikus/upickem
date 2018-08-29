package com.upickem.service;

import com.upickem.model.Game;
import com.upickem.model.SeasonType;
import com.upickem.model.Team;
import com.upickem.repository.GameRespository;
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
import java.time.LocalDate;
import java.time.Year;
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

    @Override
    public List<Game> saveGames(List<Game> games) {
        return gameRespository.save(games);
    }

    @Override
    public List<LocalDate> getDatesOfGamesForWeekFromRemote(Year calendarYear, Long nflWeek, SeasonType seasonType) {
        List<LocalDate> dates = new ArrayList<>();
        String url = constructUrl(calendarYear, nflWeek, seasonType);
        Document doc;
        try {
            doc = buildDocumentFromRemote(url);
        } catch (Exception e) {
            log.error("Could not fetch game data from server for year: " + calendarYear.toString() +
                    " and week: " + nflWeek + " and season type: " + seasonType, e);
            return dates;
        }

        NodeList nodeList = doc.getElementsByTagName("g");
        for (int i = 0; i<nodeList.getLength(); i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            String eid = namedNodeMap.getNamedItem("eid").getNodeValue();
            String dateString = eid.substring(0, 8);
            int yearInt = Integer.parseInt(dateString.substring(0, 4));
            int monthInt = Integer.parseInt(dateString.substring(4, 6));
            int dayInt = Integer.parseInt(dateString.substring(6, 8));

            LocalDate date = LocalDate.of(yearInt, monthInt, dayInt);
            dates.add(date);
        }

        return dates;
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
    public List<Game> pullGameDataFromRemoteServer(Year calendarYear, Long nflWeek, SeasonType seasonType) {

        Year year = setNflSeasonYearFromCalenderYear(calendarYear);

        List<Game> games = new ArrayList<>();
        String url = constructUrl(year, nflWeek, seasonType);
        Document doc = null;
        try {
            doc = buildDocumentFromRemote(url);
        } catch (Exception e) {
            log.error("Could not fetch game data from server for year: " + year.toString() +
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
                games.add(game);
            } else {
                Game game = new Game();
                game.setGameId(gameId);
                game.setYear(year);
                game.setWeek(nflWeek);
                game.setSeasonType(seasonType);
                game.setHomeTeam(Team.valueOf(namedNodeMap.getNamedItem("h").getNodeValue()));
                game.setAwayTeam(Team.valueOf(namedNodeMap.getNamedItem("v").getNodeValue()));
                game = updateScoresOfGameObject(game, namedNodeMap);
                games.add(game);
            }
        }

        return games;
    }

    private Year setNflSeasonYearFromCalenderYear(Year calendarYear) {
        if (LocalDate.now().getMonthValue() <= nflEndMonth) {
            calendarYear = Year.parse(String.valueOf(calendarYear.getValue()-1));
        }
        return calendarYear;
    }

    private Game updateScoresOfGameObject(Game game, NamedNodeMap namedNodeMap) {
        log.info("From inner method: " + game.getGameId().toString());

        String homeScore = namedNodeMap.getNamedItem("hs").getNodeValue();
        if (homeScore.equals("")) {
            game.setHomeScore(0L);
        } else {
            game.setHomeScore(Long.parseLong(homeScore));
        }

        String awayScore = namedNodeMap.getNamedItem("vs").getNodeValue();
        if (awayScore.equals("")) {
            game.setAwayScore(0L);
        } else {
            game.setAwayScore(Long.parseLong(awayScore));
        }

        if (namedNodeMap.getNamedItem("q").getNodeValue().equals("F")) {
            if (game.getHomeScore() > game.getAwayScore()) {
                game.setWinner(game.getHomeTeam());
            } else if (game.getAwayScore() > game.getHomeScore()) {
                game.setWinner(game.getAwayTeam());
            }
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
                    Year.parse(String.valueOf(Year.now().getValue()-1));
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

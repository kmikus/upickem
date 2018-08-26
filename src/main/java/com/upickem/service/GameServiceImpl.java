package com.upickem.service;

import com.upickem.model.Game;
import com.upickem.model.Team;
import com.upickem.repository.GameRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GameRespository gameRespository;

    //TODO update games

    @Override
    public List<Game> saveGames(List<Game> games) {
        return gameRespository.save(games);
    }

    @Override
    public List<Game> pullGameDataFromRemoteServer(Year year, Long week) {

        // TODO parameterize season
        // TODO add flag to indicate if game is over or started

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
         */

        List<Game> games = new ArrayList<>();
        String url = constructUrl(year, week);
        Document doc = null;
        try {
            doc = buildDocumentFromRemote(url);
        } catch (Exception e) {
            log.error("Could not fetch game data from server for year: " + year.toString() +
                    " and week: " + week, e);
        }
        NodeList nodeList = doc.getElementsByTagName("g");
        for (int i = 0; i < nodeList.getLength(); i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            Long gameId = Long.parseLong(namedNodeMap.getNamedItem("eid").getNodeValue());
            Optional<Game> gameFromDb = gameRespository.findByGameId(gameId);
            if(gameFromDb.isPresent()) {
                // If the game already exists in the database
                Game game = gameFromDb.get();

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

                games.add(game);
            } else {
                // When the game record doesn't exist in the database
                Game game = new Game();
                game.setGameId(gameId);
                game.setYear(year);
                game.setWeek(week);

                game.setHomeTeam(Team.valueOf(namedNodeMap.getNamedItem("h").getNodeValue()));
                String homeScore = namedNodeMap.getNamedItem("hs").getNodeValue();
                if (homeScore.equals("")) {
                    game.setHomeScore(0L);
                } else {
                    game.setHomeScore(Long.parseLong(homeScore));
                }

                game.setAwayTeam(Team.valueOf(namedNodeMap.getNamedItem("v").getNodeValue()));
                String awayScore = namedNodeMap.getNamedItem("vs").getNodeValue();
                if (awayScore.equals("")) {
                    game.setAwayScore(0L);
                } else {
                    game.setAwayScore(Long.parseLong(awayScore));
                }

                if (game.getHomeScore() > game.getAwayScore()) {
                    game.setWinner(game.getHomeTeam());
                } else if (game.getAwayScore() > game.getHomeScore()) {
                    game.setWinner(game.getAwayTeam());
                }

                games.add(game);
            }
        }

        return games;
    }

    private Document buildDocumentFromRemote(String url) throws
            ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());
        return doc;
    }

    private String constructUrl(Year year, Long week) {
        if (year.isAfter(Year.now()) || year.isBefore(Year.parse("2000"))) {
            year = Year.now();
        }
        if (week < 1 || week > 22) {
            week = 1L;
        }
        String seasonType = week > 17 ? "POST" : "REG";
        StringBuilder sb = new StringBuilder();
        sb.append("http://www.nfl.com/ajax/scorestrip?");
        sb.append("season=" + year.toString());
        sb.append("&seasonType=" + seasonType);
        sb.append("&week=" + week);

        return sb.toString();
    }
}

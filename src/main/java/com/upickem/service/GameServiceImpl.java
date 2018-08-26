package com.upickem.service;

import com.upickem.model.Game;
import com.upickem.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class GameServiceImpl implements GameService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Game> pullGameDataFromRemoteServer(Year year, Long week) {
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
            Game game = new Game();
            game.setYear(year);
            game.setWeek(week);
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            game.setHomeTeam(Team.valueOf(namedNodeMap.getNamedItem("h").getNodeValue()));
            game.setHomeScore(Long.parseLong(namedNodeMap.getNamedItem("hs").getNodeValue()));
            game.setAwayTeam(Team.valueOf(namedNodeMap.getNamedItem("v").getNodeValue()));
            game.setAwayScore(Long.parseLong(namedNodeMap.getNamedItem("vs").getNodeValue()));
            if (game.getHomeScore() > game.getAwayScore()) {
                game.setWinner(game.getHomeTeam());
            } else if(game.getAwayScore() > game.getHomeScore()) {
                game.setWinner(game.getAwayTeam());
            }
            games.add(game);
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

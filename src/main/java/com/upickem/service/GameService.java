package com.upickem.service;

import com.upickem.model.Game;
import com.upickem.model.SeasonType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.List;

@Service
public interface GameService {

    public List<Game> pullGameDataFromRemoteServer(Year year, Long week, SeasonType seasonType);

    public List<Game> saveGames(List<Game> games);

    public List<LocalDate> getDatesOfGamesForWeekFromRemote(Year year, Long week, SeasonType seasonType);

    public Long saveScheduleForYear(Year year);
}

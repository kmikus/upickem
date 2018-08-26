package com.upickem.service;

import com.upickem.model.Game;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public interface GameService {

    public List<Game> pullGameDataFromRemoteServer(Year year, Long week);

    public List<Game> saveGames(List<Game> games);
}

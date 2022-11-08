package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.service.games.StartableGame;

import java.util.UUID;

public class StartableGameImpl implements StartableGame {

    private static GameContextHolder CONTEXT = GameContextHolder.getInstance();

    @Override
    public Game startGame(Player player1) {
        Game game = new Game();
        game.setGameId(UUID.randomUUID());
        game.setStatus(GameStatus.NEW);
        game.setPlayer1(player1);
        CONTEXT.setGame(game);
        return game;
    }
}

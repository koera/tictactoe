package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.service.games.JoinableGame;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameAlreadyInProgressException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;

import java.util.UUID;

public class JoinableGameImpl implements JoinableGame {

    private GameContextHolder CONTEXT = GameContextHolder.getInstance();

    @Override
    public Game joinToGame(UUID gameId, Player player2) throws GameNotFoundException, GameAlreadyInProgressException {
        Game game = CONTEXT.getGames().get(gameId);

        if(game == null) {
            throw new GameNotFoundException(
                    String.format("Game with id %s cannot be found", gameId)
            );
        }

        if(!GameStatus.NEW.equals(game.getStatus())){
            throw new GameAlreadyInProgressException(
                    String.format("Game with id %s is already started or finished", gameId)
            );
        }

        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRESS);
        return game;
    }
}

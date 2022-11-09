package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.service.games.JoinableGame;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameStatusException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JoinableGameImpl implements JoinableGame {

    private GameContextHolder CONTEXT = GameContextHolder.getInstance();

    @Override
    public Game joinToGame(UUID gameId, Player player2) throws GameNotFoundException, GameStatusException {
        Game game = CONTEXT.getGames().get(gameId);

        if(game == null) {
            throw new GameNotFoundException(
                    String.format("Game with id %s cannot be found", gameId)
            );
        }

        if(!GameStatus.NEW.equals(game.getStatus())){
            throw new GameStatusException(
                    String.format("Game with id %s is already started or finished", gameId)
            );
        }

        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRESS);
        return game;
    }
}

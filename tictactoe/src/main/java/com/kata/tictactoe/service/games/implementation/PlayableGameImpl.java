package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.TicTacToe;
import com.kata.tictactoe.service.games.PlayableGame;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

import java.util.UUID;

public class PlayableGameImpl implements PlayableGame {

    GameContextHolder CONTEXT = GameContextHolder.getInstance();

    @Override
    public Game playGame(GamePlay gamePlay) throws GameNotFoundException, GameStatusException, GameMovesException {
        UUID gameId = gamePlay.getGameId();
        Game game = CONTEXT.getGames().get(gameId);

        if(game == null) {
            throw  new GameNotFoundException(
                    String.format("Game %s not found", gameId)
            );
        }

        validateGameStatus(gameId, game);

        if(game.getMoves().isEmpty() && !TicTacToe.X.equals(gamePlay.getType())) {
            throw new GameMovesException("First moves should be X");
        }
        game.getMoves().add(gamePlay.getType().name());

        validateAndSavePositions(gamePlay, game);

        return game;
    }

    private void validateAndSavePositions(GamePlay gamePlay, Game game) throws GameMovesException {
        if(game.getBoard()[gamePlay.getRowNumber() - 1][gamePlay.getColumnNumber() - 1] != 0) {
            throw new GameMovesException(
                    String.format(
                            "Position at row %d and col %d is already played", gamePlay.getRowNumber(), gamePlay.getColumnNumber()
                    )
            );
        }

        game.getBoard()[gamePlay.getRowNumber() - 1][gamePlay.getColumnNumber() - 1] = gamePlay.getType().getValue();
    }

    private static void validateGameStatus(UUID gameId, Game game) throws GameStatusException {
        if(!GameStatus.IN_PROGRESS.equals(game.getStatus())) {
            throw new GameStatusException(
                    String.format("Game %s is not started yet or already finished", gameId)
            );
        }
    }
}

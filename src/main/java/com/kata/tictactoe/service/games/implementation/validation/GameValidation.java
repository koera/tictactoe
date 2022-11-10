package com.kata.tictactoe.service.games.implementation.validation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.TicTacToe;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

public class GameValidation {

    public static void validateGameData(GamePlay gamePlay, Game game) throws GameStatusException, GameMovesException, GameNotFoundException {
        if(game == null) {
            throw  new GameNotFoundException(
                    String.format("Game %s not found", gamePlay.getGameId())
            );
        }
        validateGameStatus(game);
        validateMovesAndPositions(gamePlay, game);
    }

    private static void validateGameStatus(Game game) throws GameStatusException {
        if(!GameStatus.IN_PROGRESS.equals(game.getStatus())) {
            throw new GameStatusException(
                    String.format("Game %s is not started yet or already finished", game.getGameId())
            );
        }
    }

    private static void validateMovesAndPositions(GamePlay gamePlay, Game game) throws GameMovesException {
        validateFirstMove(gamePlay, game);

        if(!isFirstMove(game)) {
            validateAlternateTurn(gamePlay, game);
        }

        validatePosition(gamePlay, game);
    }

    private static void validateFirstMove(GamePlay gamePlay, Game game) throws GameMovesException {
        if(isFirstMove(game) && currentMoveIsNotX(gamePlay.getType())) {
            throw new GameMovesException("First moves should be X");
        }
    }

    private static boolean isFirstMove(Game game) {
        return game.getMoves().isEmpty();
    }

    private static void validatePosition(GamePlay gamePlay, Game game) throws GameMovesException {
        if(isPositionAlreadyPlayed(gamePlay.getRowNumber(), gamePlay.getColumnNumber(), game.getBoard())) {
            throw new GameMovesException(
                    String.format(
                            "Position at row %d and col %d is already played", gamePlay.getRowNumber(), gamePlay.getColumnNumber()
                    )
            );
        }
    }

    private static boolean isPositionAlreadyPlayed(int row, int col, int[][] board) {
        return board[row - 1][col - 1] != 0;
    }

    private static void validateAlternateTurn(GamePlay gamePlay, Game game) throws GameMovesException {
        String lastMove = game.getMoves().get(game.getMoves().size() - 1);
        String currentMove = gamePlay.getType().name();

        if (lastMove.equalsIgnoreCase(currentMove)) {
            throw new GameMovesException(
                    String.format("Wrong turn, %s's turn now", gamePlay.getType().equals(TicTacToe.X) ? TicTacToe.O : TicTacToe.X)
            );
        }
    }

    private static boolean currentMoveIsNotX(TicTacToe currentMove) {
        return !TicTacToe.X.equals(currentMove);
    }
}

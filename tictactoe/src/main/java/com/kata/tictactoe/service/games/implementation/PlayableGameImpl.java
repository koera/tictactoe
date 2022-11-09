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

import static com.kata.tictactoe.service.games.implementation.GameBoardUtility.isAllSquaresFilled;
import static com.kata.tictactoe.service.games.implementation.GameBoardUtility.typeHas3InARow;

public class PlayableGameImpl implements PlayableGame {

    private static final GameContextHolder CONTEXT = GameContextHolder.getInstance();

    @Override
    public Game playGame(GamePlay gamePlay) throws GameNotFoundException, GameStatusException, GameMovesException {

        Game game = CONTEXT.getGames().get(gamePlay.getGameId());

        validateGameData(gamePlay, game);
        saveMovesAndPosition(gamePlay, game);

        if(shouldFinishTheGame(gamePlay, game)) {
            game.setStatus(GameStatus.FINISHED);
        }

        return game;
    }

    private void validateGameData(GamePlay gamePlay, Game game) throws GameStatusException, GameMovesException, GameNotFoundException {
        if(game == null) {
            throw  new GameNotFoundException(
                    String.format("Game %s not found", gamePlay.getGameId())
            );
        }
        validateGameStatus(game);
        validateMovesAndPositions(gamePlay, game);
    }

    private boolean shouldFinishTheGame(GamePlay gamePlay, Game game) {
        boolean playerHas3Horizontal = typeHas3InARow(game.getBoard(), gamePlay.getType());
        return playerHas3Horizontal || isAllSquaresFilled(game.getBoard());
    }


    private void validateMovesAndPositions(GamePlay gamePlay, Game game) throws GameMovesException {
        validateFirstMove(gamePlay, game);

        if(!isFirstMove(game)) {
            validateAlternateTurn(gamePlay, game);
        }

        validatePosition(gamePlay, game);
    }

    private void validatePosition(GamePlay gamePlay, Game game) throws GameMovesException {
        if(isPositionAlreadyPlayed(gamePlay.getRowNumber(), gamePlay.getColumnNumber(), game.getBoard())) {
            throw new GameMovesException(
                    String.format(
                            "Position at row %d and col %d is already played", gamePlay.getRowNumber(), gamePlay.getColumnNumber()
                    )
            );
        }
    }

    private boolean isPositionAlreadyPlayed(int row, int col, int[][] board) {
        return board[row - 1][col - 1] != 0;
    }

    private void validateAlternateTurn(GamePlay gamePlay, Game game) throws GameMovesException {
        String lastMove = game.getMoves().get(game.getMoves().size() - 1);
        String currentMove = gamePlay.getType().name();

        if (lastMove.equalsIgnoreCase(currentMove)) {
            throw new GameMovesException(
                    String.format("Wrong turn, %s's turn now", gamePlay.getType().equals(TicTacToe.X) ? TicTacToe.O : TicTacToe.X)
            );
        }
    }

    private void validateFirstMove(GamePlay gamePlay, Game game) throws GameMovesException {
        if(isFirstMove(game) && currentMoveIsNotX(gamePlay.getType())) {
            throw new GameMovesException("First moves should be X");
        }
    }

    private boolean currentMoveIsNotX(TicTacToe currentMove) {
        return !TicTacToe.X.equals(currentMove);
    }

    private boolean isFirstMove(Game game) {
        return game.getMoves().isEmpty();
    }

    private void saveMovesAndPosition(GamePlay gamePlay, Game game) {
        game.getMoves().add(gamePlay.getType().name());
        game.getBoard()[gamePlay.getRowNumber() - 1][gamePlay.getColumnNumber() - 1] = gamePlay.getType().getValue();
    }

    private void validateGameStatus(Game game) throws GameStatusException {
        if(!GameStatus.IN_PROGRESS.equals(game.getStatus())) {
            throw new GameStatusException(
                    String.format("Game %s is not started yet or already finished", game.getGameId())
            );
        }
    }
}

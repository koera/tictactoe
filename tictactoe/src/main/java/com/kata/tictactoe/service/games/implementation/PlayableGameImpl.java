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

public class PlayableGameImpl implements PlayableGame {

    private static final int[][] GAME_FINISHED_COMBINATION_POSITIONS = {
            //horizontal
            {0,1,2}, {3,4,5}, {6,7,8},
            //vertical
            {0,3,6}, {1,4,7},{2,5,8},
            //diagonal
            {0,4,8}, {2,4,6}
    };

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

    private boolean typeHas3InARow(int[][] board, TicTacToe type) {

        int[] gameBoardInArray = getGameBoardAsArray(board);

        for (int[] horizontalCombinationPosition : GAME_FINISHED_COMBINATION_POSITIONS) {
            int counter = 0;
            for (int i : horizontalCombinationPosition) {
                if (gameBoardInArray[i] == type.getValue()) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int[] getGameBoardAsArray(int[][] boards) {

        int[] gameBoardInArray = new int[9];
        int counterIndex = 0;

        for (int[] board : boards) {
            for (int i : board) {
                gameBoardInArray[counterIndex] = i;
                counterIndex++;
            }
        }
        return gameBoardInArray;
    }

    private boolean isAllSquaresFilled(int[][] boards) {
        for (int[] board : boards) {
            for (int i : board) {
                if (i == 0) {
                    return false;
                }
            }
        }
        return true;
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

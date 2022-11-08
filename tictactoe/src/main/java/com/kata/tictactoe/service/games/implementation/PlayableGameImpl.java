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

    private static int[][] HORIZONTAL_COMBINATION_POSITIONS = {{0,1,2}, {3,4,5}, {6,7,8}};

    private static GameContextHolder CONTEXT = GameContextHolder.getInstance();

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
        validateMovesAndPositions(gamePlay, game);
        saveMovesAndPosition(gamePlay, game);

        if(shouldFinishTheGame(gamePlay.getType(), game)) {
            game.setStatus(GameStatus.FINISHED);
        }

        return game;
    }

    private boolean shouldFinishTheGame(TicTacToe type, Game game) {
        boolean playerHas3Horizontal = isTypeHas3InARow(game.getBoard(), type);
        return playerHas3Horizontal || isAllSquaresFilled(game.getBoard());
    }

    private boolean isTypeHas3InARow(int[][] board, TicTacToe type) {

        int[] gameBoardInArray = getGameBoardAsArray(board);

        for(int i = 0; i< HORIZONTAL_COMBINATION_POSITIONS.length; i++) {
            int counter = 0;
            for(int j = 0; j< HORIZONTAL_COMBINATION_POSITIONS[i].length; j++) {
                if(gameBoardInArray[HORIZONTAL_COMBINATION_POSITIONS[i][j]] == type.getValue()){
                    counter++;
                    if(counter == 3){
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

        for(int i = 0; i< boards.length; i++) {
            for(int j = 0; j< boards[i].length; j++) {
                gameBoardInArray[counterIndex] = boards[i][j];
                counterIndex++;
            }
        }
        return gameBoardInArray;
    }

    private boolean isAllSquaresFilled(int[][] boards) {
        for(int i = 0; i< boards.length; i++) {
            for(int j = 0; j< boards[i].length; j++) {
                if(boards[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void validateMovesAndPositions(GamePlay gamePlay, Game game) throws GameMovesException {
        if(game.getMoves().isEmpty() && !TicTacToe.X.equals(gamePlay.getType())) {
            throw new GameMovesException("First moves should be X");
        }

        if(!game.getMoves().isEmpty()) {
            String lastMove = game.getMoves().get(game.getMoves().size() - 1);

            if (lastMove.equalsIgnoreCase(gamePlay.getType().name())) {
                throw new GameMovesException(
                        String.format("Wrong turn, %s's turn now", gamePlay.getType().equals(TicTacToe.X) ? TicTacToe.O : TicTacToe.X)
                );
            }
        }

        if(game.getBoard()[gamePlay.getRowNumber() - 1][gamePlay.getColumnNumber() - 1] != 0) {
            throw new GameMovesException(
                    String.format(
                            "Position at row %d and col %d is already played", gamePlay.getRowNumber(), gamePlay.getColumnNumber()
                    )
            );
        }
    }

    private void saveMovesAndPosition(GamePlay gamePlay, Game game) {
        game.getMoves().add(gamePlay.getType().name());
        game.getBoard()[gamePlay.getRowNumber() - 1][gamePlay.getColumnNumber() - 1] = gamePlay.getType().getValue();
    }

    private void validateGameStatus(UUID gameId, Game game) throws GameStatusException {
        if(!GameStatus.IN_PROGRESS.equals(game.getStatus())) {
            throw new GameStatusException(
                    String.format("Game %s is not started yet or already finished", gameId)
            );
        }
    }
}

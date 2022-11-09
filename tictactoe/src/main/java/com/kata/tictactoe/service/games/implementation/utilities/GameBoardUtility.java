package com.kata.tictactoe.service.games.implementation.utilities;

import com.kata.tictactoe.models.TicTacToe;

public class GameBoardUtility {

    private static final int[][] GAME_FINISHED_COMBINATION_POSITIONS = {
            //horizontal
            {0,1,2}, {3,4,5}, {6,7,8},
            //vertical
            {0,3,6}, {1,4,7},{2,5,8},
            //diagonal
            {0,4,8}, {2,4,6}
    };

    public static boolean typeHas3InARow(int[][] board, TicTacToe type) {

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

    public static boolean isAllSquaresFilled(int[][] boards) {
        for (int[] board : boards) {
            for (int i : board) {
                if (i == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] getGameBoardAsArray(int[][] boards) {

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
}

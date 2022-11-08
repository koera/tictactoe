package com.kata.tictactoe.models;

import java.util.UUID;

public class GamePlay {
    private UUID gameId;
    private TicTacToe type;
    private int rowNumber;
    private int columnNumber;

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public TicTacToe getType() {
        return type;
    }

    public void setType(TicTacToe type) {
        this.type = type;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }
}

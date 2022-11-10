package com.kata.tictactoe.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

public class GamePlay {
    private UUID gameId;
    private TicTacToe type;
    @Min(value = 1, message = "rowNumber must be greater than or equal to 1")
    @Max(value = 3, message = "rowNumber must be less than or equal to 3")
    private int rowNumber;
    @Min(value = 1, message = "columnNumber must be greater than or equal to 1")
    @Max(value = 3, message = "columnNumber must be less than or equal to 3")
    private int columnNumber;

    public GamePlay() {
    }

    public GamePlay(UUID gameId, TicTacToe type, int rowNumber, int columnNumber) {
        this.gameId = gameId;
        this.type = type;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

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

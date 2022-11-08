package com.kata.tictactoe.models;

public enum TicTacToe {
    X(1),
    O(2);
    private Integer value;

    TicTacToe(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}

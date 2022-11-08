package com.kata.tictactoe.service.games.exception;

public class GameAlreadyInProgressException extends Exception {

    public GameAlreadyInProgressException(String message) {
        super(message);
    }
}

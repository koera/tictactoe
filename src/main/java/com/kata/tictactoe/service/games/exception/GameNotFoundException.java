package com.kata.tictactoe.service.games.exception;

public class GameNotFoundException extends Exception{

    public GameNotFoundException(String message) {
        super(message);
    }
}

package com.kata.tictactoe.controller;

import com.kata.tictactoe.controller.dto.ErrorMessage;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage gameNotFoundExceptionHandler(GameNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(GameStatusException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorMessage gameStatusExceptionHandler(GameStatusException exception) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.SERVICE_UNAVAILABLE.value(), new Date(), exception.getMessage());
        return errorMessage;
    }
}

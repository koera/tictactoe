package com.kata.tictactoe.controller;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.models.Winner;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

import java.util.UUID;

public interface IGameController {
    public Game startGame(Player player);
    public Game joinGame(UUID gameId, Player player2) throws GameNotFoundException, GameStatusException;
    public Game playGame(GamePlay gamePlay) throws GameMovesException, GameNotFoundException, GameStatusException;
    public Winner getWinner(UUID gameId) throws GameNotFoundException, GameStatusException;
}

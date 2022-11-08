package com.kata.tictactoe.service.games;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;

import java.util.UUID;

public interface JoinableGame {

    Game joinToGame(UUID gameId, Player player2) throws GameNotFoundException;

}

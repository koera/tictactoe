package com.kata.tictactoe.service.games;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.Winner;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

import java.util.UUID;

public interface WinnableGame {
    Winner winner(UUID gameId) throws GameNotFoundException, GameStatusException;
}

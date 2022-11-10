package com.kata.tictactoe.service.games;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

public interface PlayableGame {
    Game playGame(GamePlay gamePlay) throws GameNotFoundException, GameStatusException, GameMovesException;
}

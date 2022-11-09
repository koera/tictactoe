package com.kata.tictactoe.service.games;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.Winner;

public interface WinnableGame {
    Winner winner(Game game);
}

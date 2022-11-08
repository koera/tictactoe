package com.kata.tictactoe.service.games;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;

public interface PlayableGame {
    Game playGame(GamePlay gamePlay);
}

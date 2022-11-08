package com.kata.tictactoe.service;

import com.kata.tictactoe.service.games.JoinableGame;
import com.kata.tictactoe.service.games.PlayableGame;
import com.kata.tictactoe.service.games.StartableGame;
import com.kata.tictactoe.service.games.WinnableGame;

public interface TicToeGame extends StartableGame, JoinableGame, PlayableGame, WinnableGame {
}

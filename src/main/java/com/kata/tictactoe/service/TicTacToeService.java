package com.kata.tictactoe.service;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.models.Winner;
import com.kata.tictactoe.service.games.JoinableGame;
import com.kata.tictactoe.service.games.PlayableGame;
import com.kata.tictactoe.service.games.StartableGame;
import com.kata.tictactoe.service.games.WinnableGame;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TicTacToeService implements TicToeGame{

    private StartableGame startGame;
    private JoinableGame joinGame;
    private PlayableGame playGame;
    private WinnableGame winGame;

    public TicTacToeService(StartableGame startGame, JoinableGame joinGame, PlayableGame playGame, WinnableGame winGame) {
        this.startGame = startGame;
        this.joinGame = joinGame;
        this.playGame = playGame;
        this.winGame = winGame;
    }

    @Override
    public Game joinToGame(UUID gameId, Player player2) throws GameNotFoundException, GameStatusException {
        return joinGame.joinToGame(gameId, player2);
    }

    @Override
    public Game playGame(GamePlay gamePlay) throws GameNotFoundException, GameStatusException, GameMovesException {
        return playGame.playGame(gamePlay);
    }

    @Override
    public Game startGame(Player player1) {
        return startGame.startGame(player1);
    }

    @Override
    public Winner winner(UUID gameId) throws GameNotFoundException, GameStatusException {
        return winGame.winner(gameId);
    }
}

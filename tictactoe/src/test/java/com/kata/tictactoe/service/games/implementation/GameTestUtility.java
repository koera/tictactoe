package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.*;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

import java.util.UUID;

public class GameTestUtility {

    private UUID gameId;
    PlayableGameImpl playbaleGame = new PlayableGameImpl();

    public GameTestUtility(UUID gameId) {
        this.gameId = gameId;
    }

    public Game simulateStartAndJoinGame(Player player1, Player player2, GameContextHolder CONTEXT) {
        Game simulatedExistingGame = new Game();
        simulatedExistingGame.setGameId(gameId);
        simulatedExistingGame.setPlayer1(player1);
        simulatedExistingGame.setPlayer2(player2);
        simulatedExistingGame.setStatus(GameStatus.IN_PROGRESS);
        CONTEXT.setGame(simulatedExistingGame);
        return simulatedExistingGame;
    }

    public Game playOnPosition(TicTacToe type, int row, int col) throws GameNotFoundException, GameStatusException, GameMovesException {
        GamePlay gamePlay1 = createGamePlayFor(type, row, col);
        return playbaleGame.playGame(gamePlay1);
    }

    public GamePlay createGamePlayFor(TicTacToe type, int row, int col) {
        GamePlay gamePlay = new GamePlay();
        gamePlay.setGameId(gameId);
        gamePlay.setType(type);
        gamePlay.setColumnNumber(col);
        gamePlay.setRowNumber(row);
        return gamePlay;
    }

    public Game gameWith3XInARowHorizontally() throws GameMovesException, GameNotFoundException, GameStatusException {
        playOnPosition(TicTacToe.X, 1, 1);
        playOnPosition(TicTacToe.O, 2, 1);
        playOnPosition(TicTacToe.X, 1, 2);
        playOnPosition(TicTacToe.O, 2, 2);
        return playOnPosition(TicTacToe.X, 1, 3);
    }

    public Game gameWith3OInARowHorizontally() throws GameMovesException, GameNotFoundException, GameStatusException {
        playOnPosition(TicTacToe.X, 2, 1);
        playOnPosition(TicTacToe.O, 3, 1);
        playOnPosition(TicTacToe.X, 2, 2);
        playOnPosition(TicTacToe.O, 3, 2);
        playOnPosition(TicTacToe.X, 1, 3);
        return playOnPosition(TicTacToe.O, 3, 3);
    }
}

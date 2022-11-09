package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.TicTacToe;
import com.kata.tictactoe.models.Winner;
import com.kata.tictactoe.service.games.WinnableGame;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

import java.util.UUID;

import static com.kata.tictactoe.service.games.implementation.GameBoardUtility.isAllSquaresFilled;
import static com.kata.tictactoe.service.games.implementation.GameBoardUtility.typeHas3InARow;

public class WinnableGameImpl implements WinnableGame {

    private static final GameContextHolder CONTEXT = GameContextHolder.getInstance();
    @Override
    public Winner winner(UUID gameId) throws GameNotFoundException, GameStatusException {
        Game game = CONTEXT.getGames().get(gameId);
        if(game == null) {
            throw new GameNotFoundException(
                    String.format("Game %s not found", gameId)
            );
        }
        if(!GameStatus.FINISHED.equals(game.getStatus())){
            throw new GameStatusException(
                    String.format("Game %s is not finished yet", gameId)
            );
        }

        Winner winner = new Winner();

        checkForWinner(game, winner);

        if(noWinner(winner) && isAllSquaresFilled(game.getBoard())) {
            winner.setDraw(true);
        }

        return winner;
    }

    private void checkForWinner(Game game, Winner winner) {
        boolean isXWin = typeHas3InARow(game.getBoard(), TicTacToe.X);
        if(isXWin) {
            winner.setPlayer(game.getPlayer1());
            return;
        }

        boolean isOWin = typeHas3InARow(game.getBoard(), TicTacToe.O);
        if(isOWin) {
            winner.setPlayer(game.getPlayer2());
        }
    }

    private static boolean noWinner(Winner winner) {
        return winner.getPlayer() == null;
    }
}

package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.service.games.PlayableGame;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

import static com.kata.tictactoe.service.games.implementation.utilities.GameBoardUtility.isAllSquaresFilled;
import static com.kata.tictactoe.service.games.implementation.utilities.GameBoardUtility.typeHas3InARow;
import static com.kata.tictactoe.service.games.implementation.validation.GameValidation.validateGameData;

public class PlayableGameImpl implements PlayableGame {

    private static final GameContextHolder CONTEXT = GameContextHolder.getInstance();

    @Override
    public Game playGame(GamePlay gamePlay) throws GameNotFoundException, GameStatusException, GameMovesException {

        Game game = CONTEXT.getGames().get(gamePlay.getGameId());

        validateGameData(gamePlay, game);

        saveMovesAndPosition(gamePlay, game);

        if(shouldFinishTheGame(gamePlay, game)) {
            game.setStatus(GameStatus.FINISHED);
        }

        return game;
    }

    private void saveMovesAndPosition(GamePlay gamePlay, Game game) {
        game.getMoves().add(gamePlay.getType().name());
        game.getBoard()[gamePlay.getRowNumber() - 1][gamePlay.getColumnNumber() - 1] = gamePlay.getType().getValue();
    }

    private boolean shouldFinishTheGame(GamePlay gamePlay, Game game) {
        boolean playerHas3Horizontal = typeHas3InARow(game.getBoard(), gamePlay.getType());
        return playerHas3Horizontal || isAllSquaresFilled(game.getBoard());
    }


}

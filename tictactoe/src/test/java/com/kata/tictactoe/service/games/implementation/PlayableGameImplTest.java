package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.*;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayableGameImplTest {

    PlayableGameImpl playbaleGame = new PlayableGameImpl();
    UUID gameId = UUID.randomUUID();

    GameContextHolder CONTEXT;

    private GamePlay createGamePlayFor(UUID gameId, TicTacToe type, int row, int col) {
        GamePlay gamePlay = new GamePlay();
        gamePlay.setGameId(gameId);
        gamePlay.setType(type);
        gamePlay.setColumnNumber(col);
        gamePlay.setRowNumber(row);
        return gamePlay;
    }


    private Game simulateStartAndJoinGame(UUID existingGameID) {
        Game simulatedExistingGame = new Game();
        simulatedExistingGame.setGameId(existingGameID);
        simulatedExistingGame.setPlayer1(new Player("player-1"));
        simulatedExistingGame.setPlayer2(new Player("player-2"));
        simulatedExistingGame.setStatus(GameStatus.IN_PROGRESS);
        CONTEXT.setGame(simulatedExistingGame);
        return simulatedExistingGame;
    }

    private Game playOnPostion(TicTacToe type, int row, int col) throws GameNotFoundException, GameStatusException, GameMovesException {
        GamePlay gamePlay1 = createGamePlayFor(gameId, type, row, col);
        return playbaleGame.playGame(gamePlay1);
    }

    @BeforeEach
    void createGameData(TestInfo testInfo){
        CONTEXT = GameContextHolder.getInstance();
        if(testInfo.getDisplayName().equals("testPlayGame_only_game_in_context_can_be_played()")){
            return;
        }
        simulateStartAndJoinGame(gameId);
    }

    @Test
    void testPlayGame_only_game_in_context_can_be_played() {

        GamePlay gamePlay = createGamePlayFor(gameId, TicTacToe.X, 1 , 1);

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Game " + gameId + " not found", exception.getMessage());
    }

    @Test
    void testPlayGame_only_game_with_status_in_progress_can_be_played() throws GameNotFoundException, GameStatusException, GameMovesException {

        GamePlay gamePlay = createGamePlayFor(gameId, TicTacToe.X, 1 , 1);

        Game game = playbaleGame.playGame(gamePlay);
        game.setStatus(GameStatus.FINISHED);

        GameStatusException exception = assertThrows(GameStatusException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Game " + gameId + " is not started yet or already finished", exception.getMessage());
    }

    @Test
    void testPlayGame_first_moves_should_be_X(){

        GamePlay gamePlay = createGamePlayFor(gameId, TicTacToe.O, 1, 1);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("First moves should be X", exception.getMessage());
    }

    @Test
    void testPlayGame_played_position_should_be_saved() throws GameMovesException, GameNotFoundException, GameStatusException {

        Game game1 = playOnPostion(TicTacToe.X, 1, 2);
        assertEquals(1, game1.getBoard()[0][1]);

        Game game2 = playOnPostion(TicTacToe.O, 2, 3);
        assertEquals(2, game2.getBoard()[1][2]);
    }

    @Test
    void testPlayGame_player_cannot_play_on_played_position() throws GameMovesException, GameNotFoundException, GameStatusException {
        playOnPostion(TicTacToe.X, 2, 1);

        GamePlay gamePlay = createGamePlayFor(gameId, TicTacToe.O, 2 , 1);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Position at row 2 and col 1 is already played", exception.getMessage());
    }

    @Test
    void testPlayGame_all_moves_should_be_saved() throws GameMovesException, GameNotFoundException, GameStatusException {
        Game game = playOnPostion(TicTacToe.X, 1, 1);
        assertEquals("X", game.getMoves().get(0));

        Game game2 = playOnPostion(TicTacToe.O, 1, 2);
        assertEquals("O", game2.getMoves().get(1));
    }

    @Test
    void testPlayGame_players_alternate_placing_X_and_O() throws GameMovesException, GameNotFoundException, GameStatusException {

        Game xTurn = playOnPostion(TicTacToe.X, 1, 1);

        GamePlay xTurnAgain = createGamePlayFor(gameId, TicTacToe.X, 1 , 2);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(xTurnAgain);
        });

        assertEquals("Wrong turn, O's turn now", exception.getMessage());
    }

    @Test
    void testPlayGame_only_validate_moves_and_position_will_be_saved() throws GameMovesException, GameNotFoundException, GameStatusException {
        Game xTurn = playOnPostion(TicTacToe.X, 1, 1);
        GamePlay oPlayedInWrongPosition = createGamePlayFor(gameId, TicTacToe.O, 1, 1);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(oPlayedInWrongPosition);
        });


        assertEquals("X", xTurn.getMoves().get(0));
    }

    @Test
    void testPlayGame_finished_game_if_all_9_squares_are_filled() throws GameMovesException, GameNotFoundException, GameStatusException {
        playOnPostion(TicTacToe.X, 1, 1);
        playOnPostion(TicTacToe.O, 1, 2);
        playOnPostion(TicTacToe.X, 1, 3);

        playOnPostion(TicTacToe.O, 2, 1);
        playOnPostion(TicTacToe.X, 2, 2);
        playOnPostion(TicTacToe.O, 2, 3);

        playOnPostion(TicTacToe.X, 3, 1);
        playOnPostion(TicTacToe.O, 3, 2);
        Game game = playOnPostion(TicTacToe.X, 3, 3);

        assertEquals(GameStatus.FINISHED, game.getStatus());
    }

    @Test
    void testPlayGame_finished_game_if_one_player_has_3_in_a_row_horizontally() throws GameMovesException, GameNotFoundException, GameStatusException {
        playOnPostion(TicTacToe.X, 1, 1);
        playOnPostion(TicTacToe.O, 2, 1);
        playOnPostion(TicTacToe.X, 1, 2);
        playOnPostion(TicTacToe.O, 2, 2);
        Game game = playOnPostion(TicTacToe.X, 1, 3);

        assertEquals(GameStatus.FINISHED, game.getStatus());
    }

    @Test
    void testPlayGame_finished_game_if_one_player_has_3_in_a_row_vertically() throws GameMovesException, GameNotFoundException, GameStatusException {
        playOnPostion(TicTacToe.X, 1, 1);
        playOnPostion(TicTacToe.O, 1, 2);
        playOnPostion(TicTacToe.X, 2, 1);
        playOnPostion(TicTacToe.O, 2, 2);
        Game game = playOnPostion(TicTacToe.X, 3, 1);

        assertEquals(GameStatus.FINISHED, game.getStatus());
    }

    @Test
    void testPlayGame_finished_game_if_one_player_has_3_in_a_row_diagonally() throws GameMovesException, GameNotFoundException, GameStatusException {
        playOnPostion(TicTacToe.X, 3, 3);
        playOnPostion(TicTacToe.O, 1, 2);
        playOnPostion(TicTacToe.X, 2, 2);
        playOnPostion(TicTacToe.O, 2, 3);
        Game game = playOnPostion(TicTacToe.X, 1, 1);

        assertEquals(GameStatus.FINISHED, game.getStatus());
    }


}
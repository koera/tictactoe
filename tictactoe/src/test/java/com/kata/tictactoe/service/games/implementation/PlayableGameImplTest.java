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

    final GameTestUtility testUtility = new GameTestUtility(gameId);

    @BeforeEach
    void createGameData(TestInfo testInfo){
        CONTEXT = GameContextHolder.getInstance();
        if(testInfo.getDisplayName().equals("testPlayGame_only_game_in_context_can_be_played()")){
            return;
        }
        testUtility.simulateStartAndJoinGame(new Player("Player-1"), new Player("Player-2"), CONTEXT);
    }

    @Test
    void testPlayGame_only_game_in_context_can_be_played() {

        GamePlay gamePlay = testUtility.createGamePlayFor(TicTacToe.X, 1 , 1);

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Game " + gameId + " not found", exception.getMessage());
    }

    @Test
    void testPlayGame_only_game_with_status_in_progress_can_be_played() throws GameNotFoundException, GameStatusException, GameMovesException {

        GamePlay gamePlay = testUtility.createGamePlayFor(TicTacToe.X, 1 , 1);

        Game game = playbaleGame.playGame(gamePlay);
        game.setStatus(GameStatus.FINISHED);

        GameStatusException exception = assertThrows(GameStatusException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Game " + gameId + " is not started yet or already finished", exception.getMessage());
    }

    @Test
    void testPlayGame_first_moves_should_be_X(){

        GamePlay gamePlay = testUtility.createGamePlayFor(TicTacToe.O, 1, 1);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("First moves should be X", exception.getMessage());
    }

    @Test
    void testPlayGame_played_position_should_be_saved() throws GameMovesException, GameNotFoundException, GameStatusException {

        Game game1 = testUtility.playOnPosition(TicTacToe.X, 1, 2);
        assertEquals(1, game1.getBoard()[0][1]);

        Game game2 = testUtility.playOnPosition(TicTacToe.O, 2, 3);
        assertEquals(2, game2.getBoard()[1][2]);
    }

    @Test
    void testPlayGame_player_cannot_play_on_played_position() throws GameMovesException, GameNotFoundException, GameStatusException {
        testUtility.playOnPosition(TicTacToe.X, 2, 1);

        GamePlay gamePlay = testUtility.createGamePlayFor(TicTacToe.O, 2 , 1);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Position at row 2 and col 1 is already played", exception.getMessage());
    }

    @Test
    void testPlayGame_all_moves_should_be_saved() throws GameMovesException, GameNotFoundException, GameStatusException {
        Game game = testUtility.playOnPosition(TicTacToe.X, 1, 1);
        assertEquals("X", game.getMoves().get(0));

        Game game2 = testUtility.playOnPosition(TicTacToe.O, 1, 2);
        assertEquals("O", game2.getMoves().get(1));
    }

    @Test
    void testPlayGame_players_alternate_placing_X_and_O() throws GameMovesException, GameNotFoundException, GameStatusException {

        Game xTurn = testUtility.playOnPosition(TicTacToe.X, 1, 1);

        GamePlay xTurnAgain = testUtility.createGamePlayFor(TicTacToe.X, 1 , 2);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(xTurnAgain);
        });

        assertEquals("Wrong turn, O's turn now", exception.getMessage());
    }

    @Test
    void testPlayGame_only_validate_moves_and_position_will_be_saved() throws GameMovesException, GameNotFoundException, GameStatusException {
        Game xTurn = testUtility.playOnPosition(TicTacToe.X, 1, 1);
        GamePlay oPlayedInWrongPosition = testUtility.createGamePlayFor(TicTacToe.O, 1, 1);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(oPlayedInWrongPosition);
        });


        assertEquals("X", xTurn.getMoves().get(0));
    }

    @Test
    void testPlayGame_finished_game_if_all_9_squares_are_filled() throws GameMovesException, GameNotFoundException, GameStatusException {
        Game game = testUtility.gameWith9SquareFilled();
        assertEquals(GameStatus.FINISHED, game.getStatus());
    }

    @Test
    void testPlayGame_finished_game_if_one_player_has_3_in_a_row_horizontally() throws GameMovesException, GameNotFoundException, GameStatusException {
        Game game = testUtility.gameWith3XInARowHorizontally();
        assertEquals(GameStatus.FINISHED, game.getStatus());
    }

    @Test
    void testPlayGame_finished_game_if_one_player_has_3_in_a_row_vertically() throws GameMovesException, GameNotFoundException, GameStatusException {
       Game game = testUtility.gameWith3XInARowVertically();
       assertEquals(GameStatus.FINISHED, game.getStatus());
    }

    @Test
    void testPlayGame_finished_game_if_one_player_has_3_in_a_row_diagonally() throws GameMovesException, GameNotFoundException, GameStatusException {
        Game game = testUtility.gameWith3XInARowDiagonally();
        assertEquals(GameStatus.FINISHED, game.getStatus());
    }


}
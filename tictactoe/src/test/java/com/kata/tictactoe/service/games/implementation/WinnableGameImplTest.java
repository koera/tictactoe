package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.*;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WinnableGameImplTest {

    private final UUID gameId = UUID.randomUUID();
    private final WinnableGameImpl winnableGame = new WinnableGameImpl();
    private final GameContextHolder CONTEXT = GameContextHolder.getInstance();
    private final GameTestUtility testUtility = new GameTestUtility(gameId);
    private final Player player1 = new Player("Player-1");
    private final Player player2 = new Player("Player-2");



    @Test
    void testWinner_game_not_in_the_context_should_throw_game_not_found_exception() {
        UUID randomGameId = UUID.randomUUID();

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            Winner winner = winnableGame.winner(randomGameId);
        });

        assertEquals("Game " + randomGameId + " not found", exception.getMessage());
    }

    @Test
    void testWinner_cant_check_winner_if_game_is_not_finished_yet() throws GameMovesException, GameNotFoundException, GameStatusException {
        testUtility.simulateStartAndJoinGame(player1, player2, CONTEXT);
        testUtility.playOnPosition(TicTacToe.X, 1, 1);

        GameStatusException exception = assertThrows(GameStatusException.class, () -> {
            Winner winner = winnableGame.winner(gameId);
        });

        assertEquals("Game " + gameId + " is not finished yet", exception.getMessage());
    }

    @Test
    void testWinner_player_1_draw_3_X_in_a_row_and_should_win() throws GameMovesException, GameNotFoundException, GameStatusException {
        testUtility.simulateStartAndJoinGame(player1, player2, CONTEXT);
        testUtility.gameWith3XInARowHorizontally();

        Winner winner = winnableGame.winner(gameId);

        assertEquals(player1, winner.getPlayer());
    }
}
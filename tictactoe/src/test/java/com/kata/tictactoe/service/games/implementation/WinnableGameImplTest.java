package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Winner;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WinnableGameImplTest {

    private final WinnableGameImpl winnableGame = new WinnableGameImpl();
    private final GameContextHolder CONTEXT = GameContextHolder.getInstance();

    @Test
    void testWinner_game_not_in_the_context_should_throw_game_not_found_exception() {
        UUID randomGameId = UUID.randomUUID();

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            Winner winner = winnableGame.winner(randomGameId);
        });

        assertEquals("Game " + randomGameId + " not found", exception.getMessage());
    }
}
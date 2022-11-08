package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JoinableGameImplTest {

    Player player2;
    GameContextHolder CONTEXT;
    UUID existingGameID = UUID.randomUUID();

    JoinableGameImpl joinableGame = new JoinableGameImpl();

    @BeforeEach
    void createPlayerAndSetGameToContext(){
        CONTEXT = GameContextHolder.getInstance();
        player2 = new Player("player-2");
        simulateStartGame(existingGameID);
    }

    private Game simulateStartGame(UUID existingGameID) {
        Game simulatedExistingGame = new Game();
        simulatedExistingGame.setPlayer1(new Player("player-1"));
        simulatedExistingGame.setGameId(existingGameID);
        simulatedExistingGame.setStatus(GameStatus.NEW);
        CONTEXT.setGame(simulatedExistingGame);
        return simulatedExistingGame;
    }


    @Test
    void testJoinToGame_existing_started_game_should_be_returned() throws GameNotFoundException {
        Game game = joinableGame.joinToGame(existingGameID, player2);
        assertEquals(CONTEXT.getGames().get(existingGameID), game);
    }

    @Test
    void testJoinToGame_player2_should_be_set() throws GameNotFoundException {
        Game game = joinableGame.joinToGame(existingGameID, player2);
        assertNotNull(game.getPlayer2());
        assertEquals(player2, game.getPlayer2());
    }

    @Test
    void testJoinToGame_status_should_be_in_progress() throws GameNotFoundException {
        Game game = joinableGame.joinToGame(existingGameID, player2);
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    void testJoinToGame_game_not_found_cant_be_joined(){
        UUID randomGameId = UUID.randomUUID();

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            joinableGame.joinToGame(randomGameId, player2);
        });

        assertEquals("Game with id " + randomGameId + " cannot be found", exception.getMessage());
    }


}
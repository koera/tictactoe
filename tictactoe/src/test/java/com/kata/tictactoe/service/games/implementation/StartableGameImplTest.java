package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartableGameImplTest {

    StartableGameImpl startableGame = new StartableGameImpl();
    Player player1;

    @BeforeEach
    void createPlayerAndStartGame(){
        player1 = new Player("player1");
    }

    @Test
    void testStartGame_game_should_not_be_null() {
        Game game = startableGame.startGame(player1);
        assertNotNull(game);
    }

    @Test
    void testStartGame_game_status_should_be_new() {
        Game game = startableGame.startGame(player1);
        assertEquals(GameStatus.NEW, game.getStatus());
    }

    @Test
    void testStartGame_game_id_should_not_null(){
        Game game = startableGame.startGame(player1);
        assertNotNull(game.getGameId());
    }

    @Test
    void testStartGame_player1_should_be_set(){
        Game game = startableGame.startGame(player1);
        assertEquals(player1, game.getPlayer1());
    }

    @Test
    void testStartGame_started_game_should_be_present_in_context(){

        Game game = startableGame.startGame(player1);

        GameContextHolder gameContextHolder = GameContextHolder.getInstance();

        assertEquals(game, gameContextHolder.getGames().get(game.getGameId()));
    }
}
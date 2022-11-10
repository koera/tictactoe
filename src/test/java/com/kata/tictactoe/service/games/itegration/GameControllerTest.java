package com.kata.tictactoe.service.games.itegration;

import com.kata.tictactoe.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Player player1 = new Player("player-1");
    private final Player player2 = new Player("player-2");

    private GameControllerTestUtility testUtility;

    @BeforeEach
    void setUp(){
        testUtility = new GameControllerTestUtility(mockMvc);
    }

    @Test
    void startGame_can_start_a_new_game() throws Exception {

        Game startedGame = testUtility.startGame(player1);

        assertNotNull(startedGame.getGameId());
        assertEquals(player1.getPlayerName(), startedGame.getPlayer1().getPlayerName());
        assertEquals(GameStatus.NEW, startedGame.getStatus());
    }

    @Test
    void joinGame_can_join_to_existing_game() throws Exception {
        Game startedGame = testUtility.startGame(player1);
        Game joinedGame = testUtility.joinGame(startedGame.getGameId(), player2);

        assertEquals(player2.getPlayerName(), joinedGame.getPlayer2().getPlayerName());
        assertEquals(GameStatus.IN_PROGRESS, joinedGame.getStatus());
    }

    @Test
    void playGame_can_play_game_and_win() throws Exception {

        Game playedGame = testUtility.playGameWith3XInARow(player1, player2);

        assertEquals(GameStatus.FINISHED, playedGame.getStatus());
    }

    @Test
    void getWinner_winner_should_be_retrieved() throws Exception {

        Game playedGame = testUtility.playGameWith3XInARow(player1, player2);

        Winner winner = testUtility.getWinner(playedGame);

        assertEquals(player1.getPlayerName(), winner.getPlayer().getPlayerName());

    }
}

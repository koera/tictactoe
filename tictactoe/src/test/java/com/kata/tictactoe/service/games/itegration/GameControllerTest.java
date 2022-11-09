package com.kata.tictactoe.service.games.itegration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.tictactoe.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Player player1 = new Player("player-1");
    private final Player player2 = new Player("player-2");

    private final ObjectMapper MAPPER = new ObjectMapper();

    private Game startGame() throws Exception {
         MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/start-game")
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(player1)))
                .andExpect(status().isOk())
                .andReturn();
        return MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Game>() {
        });
    }

    private Game joinGame(UUID gameId) throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/join-game/" + gameId)
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(player2)))
                .andExpect(status().isOk())
                .andReturn();
        return MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Game>() {
        });
    }

    private Game playGameWith3XInARow() throws Exception {

        Game startedGame = startGame();
        joinGame(startedGame.getGameId());

        playGameAtPosition(startedGame.getGameId(), TicTacToe.X, 1, 1);
        playGameAtPosition(startedGame.getGameId(), TicTacToe.O, 2, 1);
        playGameAtPosition(startedGame.getGameId(), TicTacToe.X, 1, 2);
        playGameAtPosition(startedGame.getGameId(), TicTacToe.O, 2, 2);
        Game playedGame = playGameAtPosition(startedGame.getGameId(), TicTacToe.X, 1, 3);

        return playedGame;
    }

    private Game playGameAtPosition(UUID gameId, TicTacToe type, int row, int col) throws Exception{
        GamePlay gamePlay = new GamePlay(gameId, type, row, col);
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/play-game/")
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(gamePlay)))
                .andExpect(status().isOk())
                .andReturn();
        return MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Game>() {
        });
    }

    private Winner getWinner(Game playedGame) throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/tic-tac-toe/winner/" + playedGame.getGameId()))
                .andExpect(status().isOk())
                .andReturn();

        Winner winner = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Winner>() {
        });
        return winner;
    }

    @Test
    void startGame_can_start_a_new_game() throws Exception {

        Game startedGame = startGame();

        assertNotNull(startedGame.getGameId());
        assertEquals(player1.getPlayerName(), startedGame.getPlayer1().getPlayerName());
        assertEquals(GameStatus.NEW, startedGame.getStatus());
    }

    @Test
    void joinGame_can_join_to_existing_game() throws Exception {
        Game startedGame = startGame();
        Game joinedGame = joinGame(startedGame.getGameId());

        assertEquals(player2.getPlayerName(), joinedGame.getPlayer2().getPlayerName());
        assertEquals(GameStatus.IN_PROGRESS, joinedGame.getStatus());
    }

    @Test
    void playGame_can_play_game_and_win() throws Exception {

        Game playedGame = playGameWith3XInARow();

        assertEquals(GameStatus.FINISHED, playedGame.getStatus());
    }

    @Test
    void getWinner_winner_should_be_retrieved() throws Exception {

        Game playedGame = playGameWith3XInARow();

        Winner winner = getWinner(playedGame);

        assertEquals(player1.getPlayerName(), winner.getPlayer().getPlayerName());

    }
}

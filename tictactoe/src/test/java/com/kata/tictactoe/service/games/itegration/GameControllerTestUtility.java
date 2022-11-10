package com.kata.tictactoe.service.games.itegration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.tictactoe.models.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTestUtility {

    private MockMvc mockMvc;

    public GameControllerTestUtility(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private final ObjectMapper MAPPER = new ObjectMapper();

    public Game startGame(Player player1) throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/start-game")
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(player1)))
                .andExpect(status().isOk())
                .andReturn();
        return MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Game>() {
        });
    }

    public Game joinGame(UUID gameId, Player player2) throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/join-game/" + gameId)
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(player2)))
                .andExpect(status().isOk())
                .andReturn();
        return MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Game>() {
        });
    }

    public Game playGameWith3XInARow(Player player1, Player player2) throws Exception {

        Game startedGame = startGame(player1);
        joinGame(startedGame.getGameId(), player2);

        playGameAtPosition(startedGame.getGameId(), TicTacToe.X, 1, 1);
        playGameAtPosition(startedGame.getGameId(), TicTacToe.O, 2, 1);
        playGameAtPosition(startedGame.getGameId(), TicTacToe.X, 1, 2);
        playGameAtPosition(startedGame.getGameId(), TicTacToe.O, 2, 2);
        Game playedGame = playGameAtPosition(startedGame.getGameId(), TicTacToe.X, 1, 3);

        return playedGame;
    }

    public Game playGameAtPosition(UUID gameId, TicTacToe type, int row, int col) throws Exception{
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

    public Winner getWinner(Game playedGame) throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/tic-tac-toe/winner/" + playedGame.getGameId()))
                .andExpect(status().isOk())
                .andReturn();

        Winner winner = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Winner>() {
        });
        return winner;
    }
}

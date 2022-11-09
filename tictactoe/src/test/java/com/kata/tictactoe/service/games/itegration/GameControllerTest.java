package com.kata.tictactoe.service.games.itegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Player player1 = new Player("player-1");

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void startGame_can_start_a_new_game() throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/start-game")
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(player1)))
                .andExpect(status().isOk())
                .andReturn();

        Game startedGame = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<Game>() {
        });

        assertNotNull(startedGame.getGameId());
        assertEquals(player1.getPlayerName(), startedGame.getPlayer1().getPlayerName());
        assertEquals(GameStatus.NEW, startedGame.getStatus());
    }
}

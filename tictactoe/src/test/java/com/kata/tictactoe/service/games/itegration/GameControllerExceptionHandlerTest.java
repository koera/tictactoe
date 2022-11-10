package com.kata.tictactoe.service.games.itegration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.tictactoe.controller.dto.ErrorMessage;
import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.Player;
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
public class GameControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void join_to_non_existing_game_should_have_status_not_found() throws Exception{
        UUID randomGameId = UUID.randomUUID();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/join-game/" + randomGameId)
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(new Player("player-2"))))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorMessage errorMessage =  MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<ErrorMessage>() {
        });

        assertEquals("Game with id " + randomGameId + " cannot be found", errorMessage.getMessage());
        assertNotNull(errorMessage.getTimestamp());
        assertEquals(404, errorMessage.getStatusCode());
    }
}

package com.kata.tictactoe.service.games.itegration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.tictactoe.controller.dto.ErrorMessage;
import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.models.TicTacToe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper MAPPER = new ObjectMapper();

    private final Player player1 = new Player("player-1");
    private final Player player2 = new Player("player-2");

    private GameControllerTestUtility testUtility;

    private ErrorMessage joinNonExistingGame(UUID randomGameId) throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/join-game/" + randomGameId)
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(new Player("player-2"))))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorMessage errorMessage =  MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<ErrorMessage>() {
        });
        return errorMessage;
    }

    private ErrorMessage playNotJoinedGame(UUID gameId, TicTacToe type, int row, int col) throws Exception {
        GamePlay gamePlay = new GamePlay(gameId, type, row, col);
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tic-tac-toe/play-game/")
                                .contentType("application/json")
                                .content(MAPPER.writeValueAsBytes(gamePlay)))
                .andExpect(status().isServiceUnavailable())
                .andReturn();
        ErrorMessage errorMessage =  MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<ErrorMessage>() {
        });
        return errorMessage;
    }

    @BeforeEach
    void setUp(){
        testUtility = new GameControllerTestUtility(mockMvc);
    }

    @Test
    void join_to_non_existing_game_should_have_status_not_found() throws Exception{
        UUID randomGameId = UUID.randomUUID();

        ErrorMessage errorMessage = joinNonExistingGame(randomGameId);

        assertEquals("Game with id " + randomGameId + " cannot be found", errorMessage.getMessage());
        assertNotNull(errorMessage.getTimestamp());
        assertEquals(404, errorMessage.getStatusCode());
    }

    @Test
    void play_non_joined_game_should_have_game_status_exception() throws Exception {
        Game startedGame = testUtility.startGame(player1);

        ErrorMessage errorMessage = playNotJoinedGame(startedGame.getGameId(), TicTacToe.X, 1,1);

        assertEquals("Game " + startedGame.getGameId() + " is not started yet or already finished", errorMessage.getMessage());
        assertNotNull(errorMessage.getTimestamp());
        assertEquals(503, errorMessage.getStatusCode());
    }

    @Test
    void play_with_wrong_turn_should_have_bad_request() throws Exception {

        ErrorMessage errorMessage = testUtility.playGameWithWrongTurnAndGetBadRequest(player1, player2);

        assertEquals("Wrong turn, O's turn now", errorMessage.getMessage());
        assertNotNull(errorMessage.getTimestamp());
        assertEquals(400, errorMessage.getStatusCode());
    }

    @Test
    void game_play_columnNumber_and_rowNumber_should_be_validated() throws Exception {
        UUID randomGameId = UUID.randomUUID();
        ErrorMessage errorMessage = testUtility.playGameAtPosition_BadRequest(
                randomGameId,
                TicTacToe.X,
                0,
                4
        );
        assertThat(errorMessage.getMessage(), containsString("columnNumber must be less than or equal to 3"));
        assertThat(errorMessage.getMessage(), containsString("rowNumber must be greater than or equal to 1"));
        assertNotNull(errorMessage.getTimestamp());
        assertEquals(400, errorMessage.getStatusCode());
    }
}

package com.kata.tictactoe.controller;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GamePlay;
import com.kata.tictactoe.models.Player;
import com.kata.tictactoe.models.Winner;
import com.kata.tictactoe.service.TicToeGame;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
@RestController
@RequestMapping("/tic-tac-toe")
public class GameController implements IGameController {

    private TicToeGame ticToeGame;

    public GameController(TicToeGame ticToeGame) {
        this.ticToeGame = ticToeGame;
    }

    @Override
    @PostMapping("/start-game")
    public Game startGame(@RequestBody Player player) {
        return ticToeGame.startGame(player);
    }

    @Override
    @PostMapping("/join-game/{gameId}")
    public Game joinGame(@PathVariable UUID gameId, @RequestBody Player player2) throws GameNotFoundException, GameStatusException {
        return ticToeGame.joinToGame(gameId, player2);
    }

    @Override
    @PostMapping("/play-game")
    public Game playGame(@Valid @RequestBody GamePlay gamePlay) throws GameMovesException, GameNotFoundException, GameStatusException {
        return ticToeGame.playGame(gamePlay);
    }

    @Override
    @GetMapping("/winner/{gameId}")
    public Winner getWinner(@PathVariable UUID gameId) throws GameNotFoundException, GameStatusException {
        return ticToeGame.winner(gameId);
    }
}

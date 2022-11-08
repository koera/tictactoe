package com.kata.tictactoe.models;

import java.util.UUID;

public class Game {

    private UUID gameId;
    private Player player1;
    private Player player2;
    private int[][] board;
    private String[] moves;
    private GameStatus status;
    private TicTacToe winner;

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public String[] getMoves() {
        return moves;
    }

    public void setMoves(String[] moves) {
        this.moves = moves;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public TicTacToe getWinner() {
        return winner;
    }

    public void setWinner(TicTacToe winner) {
        this.winner = winner;
    }
}

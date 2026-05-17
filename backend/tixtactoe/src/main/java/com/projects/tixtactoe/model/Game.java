package com.projects.tixtactoe.model;
import java.util.UUID;

public class Game {
    private String gameId;
    private Player player1;
    private Player player2;
    private GameState status;
    private Mark[][] board;
    private Mark currentTurn;

    public Game(Player player1){
        this.gameId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.status = GameState.WAITING_FOR_PLAYER;
        this.board = new Mark[3][3];
        this.currentTurn = Mark.X;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = Mark.EMPTY;
            }
        }
    }

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public Player getPlayer1() { return player1; }
    public void setPlayer1(Player player1) { this.player1 = player1; }
    public Player getPlayer2() { return player2; }
    public void setPlayer2(Player player2) { this.player2 = player2; }
    public GameState getStatus() { return status; }
    public void setStatus(GameState status) { this.status = status; }
    public Mark[][] getBoard() { return board; }
    public void setBoard(Mark[][] board) { this.board = board; }
    public Mark getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(Mark currentTurn) { this.currentTurn = currentTurn; }
}

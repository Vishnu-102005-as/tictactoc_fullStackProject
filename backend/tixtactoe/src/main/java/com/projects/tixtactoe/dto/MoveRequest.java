package com.projects.tixtactoe.dto;
import com.projects.tixtactoe.model.Mark;

public class MoveRequest {
    private String gameId;
    private Mark playerMark;
    private int row;
    private int col;
 
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public Mark getPlayerMark() { return playerMark; }
    public void setPlayerMark(Mark playerMark) { this.playerMark = playerMark; }
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
}

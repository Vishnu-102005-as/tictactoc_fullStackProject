package com.projects.tixtactoe.service;

import com.projects.tixtactoe.dto.MoveRequest;
import com.projects.tixtactoe.exception.GameException;
import com.projects.tixtactoe.model.Game;
import com.projects.tixtactoe.model.GameState;
import com.projects.tixtactoe.model.Mark;
import com.projects.tixtactoe.model.Player;
import com.projects.tixtactoe.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Player player1) {
        Game game = new Game(player1);
        gameRepository.save(game);
        return game;
    }

    public Game connectToGame(Player player2, String gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameException("Game not found"));

        if (game.getPlayer2() != null) {
            throw new GameException("Game is already full");
        }

        game.setPlayer2(player2);
        game.setStatus(GameState.IN_PROGRESS);
        gameRepository.save(game);
        return game;
    }

    public Game makeMove(MoveRequest moveRequest) {
        Game game = gameRepository.findById(moveRequest.getGameId())
                .orElseThrow(() -> new GameException("Game not found"));

        if (game.getStatus() != GameState.IN_PROGRESS) {
            throw new GameException("Game is not in progress. Current status: " + game.getStatus());
        }
        if (game.getCurrentTurn() != moveRequest.getPlayerMark()) {
            throw new GameException("It's not your turn!");
        }
        if (moveRequest.getRow() < 0 || moveRequest.getRow() > 2 || moveRequest.getCol() < 0 || moveRequest.getCol() > 2) {
            throw new GameException("Invalid board coordinates");
        }
        if (game.getBoard()[moveRequest.getRow()][moveRequest.getCol()] != Mark.EMPTY) {
            throw new GameException("Cell is already occupied");
        }

        // Apply move
        game.getBoard()[moveRequest.getRow()][moveRequest.getCol()] = moveRequest.getPlayerMark();

        // Check win/draw conditions
        if (checkWin(game.getBoard(), moveRequest.getPlayerMark())) {
            game.setStatus(moveRequest.getPlayerMark() == Mark.X ? GameState.FINISHED_X_WINS : GameState.FINISHED_O_WINS);
        } else if (checkDraw(game.getBoard())) {
            game.setStatus(GameState.DRAW);
        } else {
            // Switch turn
            game.setCurrentTurn(game.getCurrentTurn() == Mark.X ? Mark.O : Mark.X);
        }

        gameRepository.save(game);
        return game;
    }

    private boolean checkWin(Mark[][] board, Mark mark) {
        // Check rows & columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) ||
                    (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark)) {
                return true;
            }
        }
        // Check diagonals
        return (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) ||
                (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark);
    }

    private boolean checkDraw(Mark[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    public Game getGame(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameException("Game not found"));
    }
}
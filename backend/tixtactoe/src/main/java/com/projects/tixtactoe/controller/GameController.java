package com.projects.tixtactoe.controller;

import com.projects.tixtactoe.dto.ConnectRequest;
import com.projects.tixtactoe.dto.MoveRequest;
import com.projects.tixtactoe.model.Game;
import com.projects.tixtactoe.model.Player;
import com.projects.tixtactoe.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<Game> createGame(@RequestBody Player player) {
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connectToGame(@RequestBody ConnectRequest request) {
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameId()));
    }

    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(@RequestBody MoveRequest request) {
        return ResponseEntity.ok(gameService.makeMove(request));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }
}
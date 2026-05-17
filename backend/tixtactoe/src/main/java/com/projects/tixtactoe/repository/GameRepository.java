package com.projects.tixtactoe.repository;
import com.projects.tixtactoe.model.Game;
import java.util.Optional;


public interface GameRepository {
    void save(Game game);
    Optional<Game> findById(String gameId);
}

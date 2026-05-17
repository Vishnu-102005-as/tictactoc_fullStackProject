package com.projects.tixtactoe.repository;
import com.projects.tixtactoe.model.Game;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGameRepository implements GameRepository {
    private final Map<String, Game> gameStore = new ConcurrentHashMap<>();

    @Override
    public void save(Game game) {
        gameStore.put(game.getGameId(), game);
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return  Optional.ofNullable(gameStore.get(gameId));
    }
}

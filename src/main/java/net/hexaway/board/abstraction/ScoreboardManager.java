package net.hexaway.board.abstraction;

import net.hexaway.board.builder.ScoreboardModelBuilder;
import net.hexaway.board.model.ScoreboardModel;
import net.hexaway.board.repository.Repository;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ScoreboardManager {

    void setScoreboard(Player player, String modelId);

    void removeScoreboard(Player player, String modelId);

    void unregisterScoreboard(UUID uuid);

    void registerModel(String id, ScoreboardModel scoreboardModel);

    void registerModel(ScoreboardModelBuilder scoreboardModelBuilder);

    void unregisterModel(String id);

    ScoreboardModel getModel(String modelId);

    Optional<HexaBoard> getBoard(Player player);

    Set<ScoreboardModel> getModels();

    Map<UUID, HexaBoard> getBoards();

    void replace(String id, ScoreboardModel scoreboardModel);

    default void replace(ScoreboardModelBuilder scoreboardModelBuilder) {
        ScoreboardModel scoreboardModel = scoreboardModelBuilder.build();

        if (scoreboardModel != null)
            replace(scoreboardModel.getId(), scoreboardModel);
    }

    default void registerIfAbsent(ScoreboardModelBuilder scoreboardModelBuilder) {
        ScoreboardModel scoreboardModel = scoreboardModelBuilder.build();

        if (!exists(scoreboardModel.getId()))
            registerModel(scoreboardModel.getId(), scoreboardModel);
    }

    boolean usePlaceholderAPI();

    boolean exists(String id);

    Repository getScoreboardRepository();

    void saveModels();

    void stop();

    void resume();
}
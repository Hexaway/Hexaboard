package net.hexaway.board;

import com.github.imthenico.repositoryhelper.core.repository.Repository;
import net.hexaway.board.abstraction.HexaBoard;
import net.hexaway.board.abstraction.ScoreboardManager;
import net.hexaway.board.builder.ScoreboardModelBuilder;
import net.hexaway.board.model.ScoreboardModel;
import net.hexaway.board.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleScoreboardManager implements ScoreboardManager {

    private final Map<String, ScoreboardModel> scoreboardModelMap;

    private final Map<UUID, HexaBoard> scoreboardMap;

    private Repository<ScoreboardModel> scoreboardModelRepository;

    private final JavaPlugin javaPlugin;

    private BukkitTask bukkitTask;

    private final boolean usePlaceholderAPI;

    public SimpleScoreboardManager(JavaPlugin javaPlugin, Repository<ScoreboardModel> scoreboardModelRepository, boolean usePlaceholderAPI) {
        Validate.notNull(javaPlugin, "javaPlugin");
        Validate.notNull(scoreboardModelRepository, "scoreboardModelRepository");

        this.scoreboardModelRepository = scoreboardModelRepository;
        this.scoreboardModelMap = new ConcurrentHashMap<>();
        this.scoreboardMap = new ConcurrentHashMap<>();
        this.usePlaceholderAPI = checkPlaceholderAPI(usePlaceholderAPI);

        scoreboardModelRepository.findAll().forEach(scoreboardModel ->
                registerModel(scoreboardModel.getId(), scoreboardModel));

        this.javaPlugin = javaPlugin;
        start();
    }

    public SimpleScoreboardManager(JavaPlugin javaPlugin, boolean usePlaceholderAPI) {
        this.scoreboardModelMap = new ConcurrentHashMap<>();
        this.scoreboardMap = new ConcurrentHashMap<>();
        this.usePlaceholderAPI = checkPlaceholderAPI(usePlaceholderAPI);

        this.javaPlugin = javaPlugin;
        start();
    }

    @Override
    public void setScoreboard(Player player, String scoreboardId) {
        Validate.notNull(player, "player cannot be null");
        if (scoreboardId == null)
            return;

        ScoreboardModel scoreboardModel = scoreboardModelMap.get(scoreboardId);

        if (scoreboardModel != null) {
            UUID uuid = player.getUniqueId();

            if (scoreboardMap.containsKey(uuid))
                removeScoreboard(player);

            HexaBoard hexaBoard = new SimpleHexaBoardImpl(scoreboardModel, this, player);
            scoreboardMap.put(player.getUniqueId(), hexaBoard);
            hexaBoard.setScoreboard();
        }
    }

    @Override
    public void removeScoreboard(Player player) {
        Validate.notNull(player, "player cannot be null");

        HexaBoard hexaBoard = scoreboardMap.get(player.getUniqueId());

        if (hexaBoard != null)
            hexaBoard.delete();
    }

    @Override
    public void registerScoreboard(UUID uuid, HexaBoard board) {
        Validate.notNull(uuid, "uuid");
        Validate.notNull(board, "board");

        this.scoreboardMap.put(uuid, board);
    }

    @Override
    public void unregisterScoreboard(UUID uuid) {
        scoreboardMap.remove(uuid);
    }

    @Override
    public void unregisterModel(String id) {
        scoreboardModelMap.remove(id);
    }

    @Override
    public ScoreboardModel getModel(String modelId) {
        return scoreboardModelMap.get(modelId);
    }

    @Override
    public Optional<HexaBoard> getBoard(Player player) {
        return Optional.ofNullable(scoreboardMap.get(player.getUniqueId()));
    }

    @Override
    public Set<ScoreboardModel> getModels() {
        return new HashSet<>(scoreboardModelMap.values());
    }

    @Override
    public Map<UUID, HexaBoard> getBoards() {
        return Collections.unmodifiableMap(scoreboardMap);
    }

    @Override
    public void replace(String id, ScoreboardModel scoreboardModel) {
        Validate.notNull(id, "id");
        Validate.notNull(scoreboardModel, "scoreboardModel");

        scoreboardModelMap.replace(id, scoreboardModel);
    }

    @Override
    public boolean usePlaceholderAPI() {
        return usePlaceholderAPI;
    }

    @Override
    public boolean exists(String id) {
        return scoreboardModelMap.containsKey(id);
    }

    @Override
    public void registerModel(String id, ScoreboardModel scoreboardModel) {
        Validate.notNull(id, "id");
        Validate.notNull(scoreboardModel, "scoreboardModel");

        Validate.isTrue(!scoreboardModelMap.containsKey(id), "already exist scoreboard model \"" + id + "\"");

        scoreboardModelMap.put(id, scoreboardModel);
    }

    @Override
    public void registerModel(ScoreboardModelBuilder scoreboardModelBuilder) {
        Validate.notNull(scoreboardModelBuilder, "scoreboardModelBuilder");

        ScoreboardModel scoreboardModel = scoreboardModelBuilder.build();

        registerModel(scoreboardModel.getId(), scoreboardModel);
    }

    @Override
    public Repository<ScoreboardModel> getScoreboardRepository() {
        return scoreboardModelRepository;
    }

    @Override
    public void saveModels() {
        Validate.notNull(scoreboardModelRepository, "no repository provided");

        scoreboardModelMap.forEach((s, scoreboardModel) -> scoreboardModelRepository.save(s, scoreboardModel));
    }

    @Override
    public void stop() {
        if (bukkitTask != null) {
            this.bukkitTask.cancel();
            this.bukkitTask = null;
        }
    }

    @Override
    public void resume() {
        if (bukkitTask == null) {
            start();
        }
    }

    private void start() {
        this.bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, this::updateScoreboards, 0, 1);
    }

    private void updateScoreboards() {
        for (HexaBoard value : scoreboardMap.values()) {
            value.update();
        }
    }

    private boolean checkPlaceholderAPI(boolean b) {
        if (b) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
                throw new UnsupportedOperationException("PlaceholderAPI not found");
            }

            return true;
        }

        return false;
    }
}
package net.hexaway.board.repository;

import net.hexaway.board.model.ScoreboardModel;
import net.hexaway.board.repository.serialization.ScoreboardModelDeserializer;
import net.hexaway.board.repository.serialization.ScoreboardModelSerializer;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class YamlScoreboardModelRepository implements Repository<ScoreboardModel> {

    protected final File folder;

    protected final JavaPlugin javaPlugin;

    protected final ScoreboardModelSerializer<Map<String, Object>> scoreboardModelSerializer;

    protected final ScoreboardModelDeserializer<Map<String, Object>> scoreboardModelDeserializer;

    public YamlScoreboardModelRepository(
            File folder,
            JavaPlugin javaPlugin,
            ScoreboardModelSerializer<Map<String, Object>> scoreboardModelSerializer,
            ScoreboardModelDeserializer<Map<String, Object>> scoreboardModelDeserializer
    ) {
        Validate.notNull(scoreboardModelSerializer, "scoreboardModelSerializer");
        Validate.notNull(scoreboardModelDeserializer, "scoreboardModelDeserializer");
        Validate.notNull(javaPlugin);
        Validate.notNull(folder, "folder");

        this.scoreboardModelSerializer = scoreboardModelSerializer;
        this.scoreboardModelDeserializer = scoreboardModelDeserializer;
        this.javaPlugin = javaPlugin;

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new UnsupportedOperationException("cannot create directory" + folder.getAbsolutePath());
            }
        }

        this.folder = folder;
    }

    @Override
    public void save(String id, ScoreboardModel object) {
        try {
            File file = new File(folder, id.endsWith(".yml") ? id : id + ".yml");

            if (!file.exists()) {
                file.createNewFile();
            }

            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

            save(file, yamlConfiguration, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        new File(folder, id).delete();
    }

    @Override
    public ScoreboardModel find(String id) {
        File file = new File(folder, id.endsWith(".yml") ? id : id + ".yml");

        if (file.exists()) {
            return getFromConfig(YamlConfiguration.loadConfiguration(file));
        }

        return null;
    }

    @Override
    public Set<ScoreboardModel> findAll() {
        Set<ScoreboardModel> boardMap = new HashSet<>();

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files == null)
            return Collections.emptySet();

        for (File file : files) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

            ScoreboardModel scoreboardModel = getFromConfig(yamlConfiguration);

            if (scoreboardModel != null)
                boardMap.add(scoreboardModel);
        }
        return boardMap;
    }

    private void save(File file, YamlConfiguration yamlConfiguration, ScoreboardModel scoreboardModel) throws IOException {
        yamlConfiguration.set("scoreboard", scoreboardModelSerializer.serializeBoard(scoreboardModel));
        yamlConfiguration.save(file);
    }

    public ScoreboardModel getFromConfig(YamlConfiguration yamlConfiguration) {
        if (yamlConfiguration != null) {
            ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("scoreboard");

            if (configurationSection != null) {
                return scoreboardModelDeserializer.deserializeBoard(configurationSection.getValues(true));
            }
        }
        return null;
    }
}
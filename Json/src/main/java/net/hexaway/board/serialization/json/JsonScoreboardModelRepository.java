package net.hexaway.board.serialization.json;

import net.hexaway.board.model.ScoreboardModel;
import net.hexaway.board.repository.Repository;
import net.hexaway.board.repository.serialization.ScoreboardModelDeserializer;
import net.hexaway.board.repository.serialization.ScoreboardModelSerializer;
import net.hexaway.board.serialization.json.file.JsonFile;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JsonScoreboardModelRepository implements Repository<ScoreboardModel> {

    private final ScoreboardModelSerializer<String> scoreboardModelSerializer;

    private final ScoreboardModelDeserializer<String> scoreboardModelDeserializer;

    private final File folder;

    public JsonScoreboardModelRepository(
            ScoreboardModelSerializer<String> scoreboardModelSerializer,
            ScoreboardModelDeserializer<String> scoreboardModelDeserializer,
            File folder
    ) {
        Validate.notNull(scoreboardModelSerializer, "scoreboardModelSerializer");
        Validate.notNull(scoreboardModelDeserializer, "scoreboardModelDeserializer");
        Validate.notNull(folder, "folder");

        this.scoreboardModelSerializer = scoreboardModelSerializer;
        this.scoreboardModelDeserializer = scoreboardModelDeserializer;

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new UnsupportedOperationException("cannot create directory" + folder.getAbsolutePath());
            }
        }

        this.folder = folder;
    }

    @Override
    public void save(String id, ScoreboardModel object) {
        JsonFile jsonFile = new JsonFile(folder, id);

        jsonFile.writeJson(scoreboardModelSerializer.serializeBoard(object));
    }

    @Override
    public void delete(String id) {
        JsonFile jsonFile = new JsonFile(folder, id);

        File file = jsonFile.getFile();
        Validate.isTrue(file.isFile(), file.getAbsolutePath() + " is not a file");

        file.delete();
    }

    @Override
    public ScoreboardModel find(String id) {
        return getFromFile(new File(folder, id));
    }

    @Override
    public Set<ScoreboardModel> findAll() {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null)
            return Collections.emptySet();

        Set<ScoreboardModel> scoreboardModels = new HashSet<>();

        for (File file : files) {
            ScoreboardModel scoreboardModel = getFromFile(file);

            if (scoreboardModel == null)
                continue;

            scoreboardModels.add(scoreboardModel);
        }

        return scoreboardModels;
    }

    private ScoreboardModel getFromFile(File file) {
        if (!file.exists() || !file.getName().endsWith(".json"))
            return null;

        try {
            JsonFile jsonFile = new JsonFile(file);
            return scoreboardModelDeserializer.deserializeBoard(jsonFile.getJson());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
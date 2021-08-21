package net.hexaway.board.serialization.bukkit;

import com.github.imthenico.repositoryhelper.core.serialization.Serializer;
import net.hexaway.board.model.ScoreboardModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BukkitScoreboardModelSerializer implements Serializer<Map<String, Object>, ScoreboardModel> {

    @Override
    public Map<String, Object> serialize(Object o) {
        if (!(o instanceof ScoreboardModel)) {
            return Collections.emptyMap();
        }
        ScoreboardModel scoreboardModel = (ScoreboardModel) o;

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("title", scoreboardModel.getTitle().serialize());

        Map<String, Object> lines = new HashMap<>();
        scoreboardModel.getLines().forEach((k, v) -> lines.put(k.toString(), v.serialize()));

        objectMap.put("lines", lines);
        objectMap.put("id", scoreboardModel.getId());

        return objectMap;
    }

    @Override
    public Class<ScoreboardModel> dataInputType() {
        return ScoreboardModel.class;
    }

    @Override
    public Class<Map<String, Object>> dataOutputType() {
        return (Class<Map<String, Object>>) (Class) Map.class;
    }
}
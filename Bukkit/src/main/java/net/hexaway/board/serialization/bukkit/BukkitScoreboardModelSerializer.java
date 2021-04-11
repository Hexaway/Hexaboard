package net.hexaway.board.serialization.bukkit;

import net.hexaway.board.repository.serialization.ScoreboardModelSerializer;
import net.hexaway.board.model.ScoreboardModel;

import java.util.HashMap;
import java.util.Map;

public class BukkitScoreboardModelSerializer extends ScoreboardModelSerializer<Map<String, Object>> {

    @Override
    public Map<String, Object> serializeBoard(ScoreboardModel scoreboardModel) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("title", scoreboardModel.getTitle().serialize());

        Map<String, Object> lines = new HashMap<>();
        scoreboardModel.getLines().forEach((k, v) -> lines.put(k.toString(), v.serialize()));

        objectMap.put("lines", lines);
        objectMap.put("id", scoreboardModel.getId());
        return objectMap;
    }
}
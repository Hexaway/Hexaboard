package net.hexaway.board.serialization.map;

import net.hexaway.board.repository.serialization.ScoreboardModelSerializer;
import net.hexaway.board.model.ScoreboardModel;

import java.util.HashMap;
import java.util.Map;

public class DefaultScoreboardModelSerializer extends ScoreboardModelSerializer<Map<String, Object>> {

    @Override
    public Map<String, Object> serializeBoard(ScoreboardModel scoreboardContext) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("title", scoreboardContext.getTitle());
        objectMap.put("lines", scoreboardContext.getLines());
        objectMap.put("id", scoreboardContext.getId());
        return objectMap;
    }
}
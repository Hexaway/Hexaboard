package net.hexaway.board.serialization.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.hexaway.board.model.ScoreboardModel;
import net.hexaway.board.repository.serialization.ScoreboardModelDeserializer;
import net.hexaway.board.repository.serialization.ScoreboardModelSerializer;
import net.hexaway.board.serialization.bukkit.BukkitScoreboardModelDeserializer;
import net.hexaway.board.serialization.bukkit.BukkitScoreboardModelSerializer;

import java.io.IOException;
import java.util.Map;

public final class JsonSerializerFactory {

    public static ScoreboardModelSerializer<String> newGsonSerializer(Gson gson) {
        return new ScoreboardModelSerializer<String>() {

            private final Gson serializer = gson != null ? gson : new Gson();

            @Override
            public String serializeBoard(ScoreboardModel scoreboardModel) {
                return serializer.toJson(scoreboardModel);
            }
        };
    }

    public static ScoreboardModelDeserializer<String> newGsonDeserializer(Gson gson) {
        return new ScoreboardModelDeserializer<String>() {

            private final Gson deserializer = gson != null ? gson : new Gson();

            @Override
            public ScoreboardModel deserializeBoard(String dataType) throws IllegalArgumentException {
                return deserializer.fromJson(dataType, ScoreboardModel.class);
            }
        };
    }

    public static ScoreboardModelSerializer<String> newJacksonSerializer(ObjectMapper objectMapper) {
        return new ScoreboardModelSerializer<String>() {

            private final ObjectMapper serializer = objectMapper != null ? objectMapper : new ObjectMapper();

            private final ScoreboardModelSerializer<Map<String, Object>> delegate = new BukkitScoreboardModelSerializer();

            @Override
            public String serializeBoard(ScoreboardModel scoreboardModel) {
                try {
                    return serializer.writeValueAsString(delegate.serializeBoard(scoreboardModel));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                return "{}";
            }
        };
    }

    public static ScoreboardModelDeserializer<String> newJacksonDeserializer(ObjectMapper objectMapper) {
        return new ScoreboardModelDeserializer<String>() {

            private final ObjectMapper serializer = objectMapper != null ? objectMapper : new ObjectMapper();

            private final ScoreboardModelDeserializer<Map<String, Object>> delegate = new BukkitScoreboardModelDeserializer();

            @Override
            @SuppressWarnings("unchecked")
            public ScoreboardModel deserializeBoard(String dataType) throws IllegalArgumentException {
                try {
                    return delegate.deserializeBoard(serializer.readValue(dataType, Map.class));
                } catch (IOException e) {
                    throw new IllegalArgumentException("Invalid JSON", e);
                }
            }
        };
    }
}
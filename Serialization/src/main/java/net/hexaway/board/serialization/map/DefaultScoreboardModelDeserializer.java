package net.hexaway.board.serialization.map;

import net.hexaway.board.repository.serialization.ScoreboardModelDeserializer;
import net.hexaway.board.builder.ScoreboardModelBuilder;
import net.hexaway.board.model.AnimatableObjectModel;
import net.hexaway.board.model.AnimatedLineModel;
import net.hexaway.board.model.ScoreboardModel;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.stream.Collectors;

public class DefaultScoreboardModelDeserializer implements ScoreboardModelDeserializer<Map<String, Object>> {

    @Override
    public ScoreboardModel deserializeBoard(Map<String, Object> dataType) throws IllegalArgumentException {
        if (!dataType.containsKey("id"))
            throw new IllegalArgumentException("dataType does not have id key");

        String id = (String) dataType.get("id");

        ScoreboardModelBuilder builder = new ScoreboardModelBuilder(id);

        try {
            AnimatableObjectModel title = asAnimatableObject(dataType.get("title"));
            builder.setTitle(title);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Map<String, Object> lines = asMap(dataType.get("lines"));

        for (Object value : lines.values().stream().filter(o -> o instanceof Map || o instanceof ConfigurationSection).collect(Collectors.toList())) {
            AnimatedLineModel animatedLineModel = AnimatedLineModel.deserialize(asMap(value));

            if (animatedLineModel != null) {
                builder.addLine(animatedLineModel.getPosition(), animatedLineModel);
            }
        }

        return builder.build();
    }

    private Map<String, Object> asMap(Object o) {
        if (o instanceof Map) {
            return (Map<String, Object>) o;
        } else if (o instanceof ConfigurationSection) {
            return ((ConfigurationSection) o).getValues(true);
        }

        throw new IllegalArgumentException("unexpected value");
    }

    private AnimatableObjectModel asAnimatableObject(Object o) {
        if (o instanceof AnimatableObjectModel) {
            return (AnimatableObjectModel) o;
        } else if (o instanceof Map) {
            return AnimatableObjectModel.deserialize((Map<String, Object>) o);
        } else if (o instanceof ConfigurationSection) {
            return AnimatableObjectModel.deserialize(((ConfigurationSection) o).getValues(true));
        }

        throw new IllegalArgumentException("unexpected value");
    }
}
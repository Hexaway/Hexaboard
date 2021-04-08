package net.hexaway.board.model;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;

import java.util.Map;

@SerializableAs("AnimatedLine")
public class AnimatedLineModel extends AnimatableObjectModel {

    private final int position;

    public AnimatedLineModel(int updateTime, boolean colorize, int position, String... text) {
        super(updateTime, colorize, text);
        this.position = position;
    }

    public AnimatedLineModel(AnimatableObjectModel delegate, int position) {
        super(delegate.updateTime, delegate.colorize, delegate.text);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = super.serialize();
        objectMap.put("position", position);
        return objectMap;
    }

    public static AnimatedLineModel deserialize(Map<String, Object> objectMap) {
        AnimatableObjectModel delegate = AnimatableObjectModel.deserialize(objectMap);

        if (delegate != null && objectMap.containsKey("position")) {
            return new AnimatedLineModel(delegate, NumberConversions.toInt(objectMap.get("position")));
        }

        return null;
    }
}
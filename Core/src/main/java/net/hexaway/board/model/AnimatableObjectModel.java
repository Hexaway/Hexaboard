package net.hexaway.board.model;

import net.hexaway.board.abstraction.ScoreboardLine;
import net.hexaway.board.abstraction.Title;
import net.hexaway.board.animation.DynamicScoreboardLine;
import net.hexaway.board.animation.DynamicTitle;
import net.hexaway.board.util.ArrayUtils;
import net.hexaway.board.util.TextUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnimatableObjectModel implements ConfigurationSerializable {

    protected final String[] text;

    protected final int updateTime;

    protected final boolean colorize;

    public AnimatableObjectModel(int updateTime, boolean colorize, String... text) {
        this.text = colorize ? TextUtils.color(text) : text;
        this.updateTime = updateTime;
        this.colorize = colorize;
    }

    public String[] getText() {
        return text;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public ScoreboardLine newLine(int position) {
        return new DynamicScoreboardLine(this, position);
    }

    public Title newTitle() {
        return new DynamicTitle(this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("text", Arrays.asList(TextUtils.replaceColorChar(text)));
        objectMap.put("updateTime", updateTime);
        objectMap.put("colorize", colorize);
        return objectMap;
    }

    public static AnimatableObjectModel deserialize(Map<String, Object> objectMap) throws IllegalArgumentException {
        if (objectMap != null) {
            int updateTime = NumberConversions.toInt(objectMap.get("updateTime"));

            String[] text = ArrayUtils.toArray(objectMap.get("text"));

            Object colorize = objectMap.getOrDefault("colorize", false);

            return new AnimatableObjectModel(updateTime, colorize instanceof Boolean ? (Boolean) colorize : false, text);
        }

        return null;
    }
}
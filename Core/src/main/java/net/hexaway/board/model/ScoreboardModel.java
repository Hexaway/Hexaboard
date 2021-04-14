package net.hexaway.board.model;

import net.hexaway.board.abstraction.ScoreboardLine;
import org.apache.commons.lang.Validate;

import java.util.*;

public final class ScoreboardModel {

    private final AnimatableObjectModel title;

    private final Map<Integer, AnimatedLineModel> lines;

    private final String id;

    public ScoreboardModel(String id, AnimatableObjectModel title, List<AnimatedLineModel> lines) {
        Validate.notNull(title);
        Validate.notNull(lines);
        Validate.notNull(id);

        this.title = title;
        this.lines = new HashMap<>();

        for (AnimatedLineModel line : lines) {
            this.lines.put(line.getPosition(), line);
        }

        this.id = id;
    }

    public AnimatableObjectModel getTitle() {
        return title;
    }

    public Map<Integer, AnimatedLineModel> getLines() {
        return Collections.unmodifiableMap(lines);
    }

    public List<ScoreboardLine> getScoreboardLines() {
        List<ScoreboardLine> scoreboardLines = new ArrayList<>();

        int i = 0;
        for (Map.Entry<Integer, AnimatedLineModel> entry : lines.entrySet()) {
            if (i >= 15)
                break;

            scoreboardLines.add(entry.getValue().newLine());
            i++;
        }

        return Collections.unmodifiableList(scoreboardLines);
    }

    public String getId() {
        return id;
    }
}
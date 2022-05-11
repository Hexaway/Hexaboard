package net.hexaway.board.model;

import net.hexaway.board.abstraction.ScoreboardElement;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class ScoreboardModelBuilder {

    private ScoreboardElement title;
    private final Map<Integer, ScoreboardElement> scoreboardLines;

    public ScoreboardModelBuilder() {
        this.scoreboardLines = new LinkedHashMap<>();
    }

    public ScoreboardModelBuilder title(ScoreboardElement title) {
        this.title = requireNonNull(title, "title");
        return this;
    }

    public ScoreboardModelBuilder line(int position, String text) {
        requireNonNull(text, "text");

        return line(position, ScoreboardElement.simple(text));
    }

    public ScoreboardModelBuilder line(int position, ScoreboardElement element) {
        requireNonNull(element, "element");

        this.scoreboardLines.put(position, element.copy());
        return this;
    }

    public ScoreboardModelBuilder lines(Map<Integer, ScoreboardElement> scoreboardLines) {
        requireNonNull(scoreboardLines, "scoreboardLines");

        scoreboardLines.forEach(this::line);
        return this;
    }

    public ScoreboardModelBuilder simpleLines(String... lines) {
        for (int i = 0; i < lines.length; i++) {
            line(i, lines[i]);
        }

        return this;
    }

    public ScoreboardPack build() throws IllegalArgumentException {
        return new ScoreboardPack(title, scoreboardLines);
    }
}
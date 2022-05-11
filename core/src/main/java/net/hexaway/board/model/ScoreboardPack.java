package net.hexaway.board.model;

import net.hexaway.board.abstraction.ComplexBoard;
import net.hexaway.board.abstraction.ScoreboardElement;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class ScoreboardPack {

    private final ScoreboardElement title;
    private final Map<Integer, ScoreboardElement> lines;

    public ScoreboardPack(ScoreboardElement title, Map<Integer, ScoreboardElement> lines) {
        this.title = Objects.requireNonNull(title);
        this.lines = Collections.unmodifiableMap(lines);
    }

    public ScoreboardElement getTitle() {
        return title.copy();
    }

    public Map<Integer, ScoreboardElement> getLines() {
        return lines;
    }

    public void install(ComplexBoard board) {
        board.setComplexTitle(title);
        lines.forEach(board::setComplexLine);
    }
}
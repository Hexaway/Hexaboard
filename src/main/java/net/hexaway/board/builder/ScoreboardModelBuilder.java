package net.hexaway.board.builder;

import net.hexaway.board.model.AnimatableObjectModel;
import net.hexaway.board.model.AnimatedLineModel;
import net.hexaway.board.model.ScoreboardModel;
import org.apache.commons.lang.Validate;

import java.util.*;

public class ScoreboardModelBuilder {

    private final String id;

    private AnimatableObjectModel title;

    private final List<AnimatedLineModel> scoreboardLines;

    public ScoreboardModelBuilder(String id) {
        this.id = id;
        this.scoreboardLines = new ArrayList<>();
    }

    public ScoreboardModelBuilder setTitle(AnimatableObjectModel title) {
        Validate.notNull(title, "title");

        this.title = title;
        return this;
    }

    public ScoreboardModelBuilder addLine(String text, int position) {
        Validate.notNull(text, "text");

        return addLine(position, new AnimatableObjectModel(0, false, text));
    }

    public ScoreboardModelBuilder addLine(String text, int position, int updateTime) {
        Validate.notNull(text, "text");

        return addLine(position, new AnimatableObjectModel(updateTime, false, text));
    }

    public ScoreboardModelBuilder addLine(int position, AnimatableObjectModel animatableObjectModel) {
        Validate.notNull(animatableObjectModel, "scoreboardLine");

        this.scoreboardLines.add(new AnimatedLineModel(animatableObjectModel, position));
        return this;
    }

    public ScoreboardModelBuilder addLine(AnimatedLineModel animatedLineModel) {
        Validate.notNull(animatedLineModel, "scoreboardLine");

        this.scoreboardLines.add(animatedLineModel);
        return this;
    }

    public ScoreboardModelBuilder addLines(List<AnimatedLineModel> animatedLines) {
        Validate.notNull(scoreboardLines, "scoreboardLines");

        this.scoreboardLines.addAll(animatedLines);
        return this;
    }

    public ScoreboardModel build() throws IllegalArgumentException {
        title = title != null ? title : new AnimatableObjectModel(0, false, "title");

        Validate.isTrue(!scoreboardLines.isEmpty(), "scoreboard lines cannot be empty");

        return new ScoreboardModel(id, title, scoreboardLines);
    }
}
package net.hexaway.board.animation;

import net.hexaway.board.abstraction.ScoreboardLine;
import net.hexaway.board.model.AnimatableObjectModel;

public final class DynamicScoreboardLine implements ScoreboardLine {

    private final int pos;

    private final DynamicText dynamicText;

    public DynamicScoreboardLine(AnimatableObjectModel animatableObjectModel, int position) {
        this(position, animatableObjectModel.getUpdateTime(), animatableObjectModel.getText());
    }

    public DynamicScoreboardLine(int position, int updateTime, String... text) {
        this.pos = position;
        this.dynamicText = new DynamicText(updateTime, text);
    }

    @Override
    public String get() {
        return dynamicText.getNextLine();
    }

    @Override
    public int position() {
        return pos;
    }

    @Override
    public void update() {
        dynamicText.getTickableCounter().nextTick();
    }
}
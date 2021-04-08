package net.hexaway.board.animation;

import net.hexaway.board.abstraction.Title;
import net.hexaway.board.model.AnimatableObjectModel;

public final class DynamicTitle implements Title {

    private final DynamicText dynamicText;

    public DynamicTitle(AnimatableObjectModel animatableObjectModel) {
        this(animatableObjectModel.getUpdateTime(), animatableObjectModel.getText());
    }

    public DynamicTitle(int updateTime, String... text) {
        this.dynamicText = new DynamicText(updateTime, text);
    }

    @Override
    public String get() {
        return dynamicText.getNextLine();
    }

    @Override
    public void update() {
        dynamicText.getTickableCounter().nextTick();
    }
}
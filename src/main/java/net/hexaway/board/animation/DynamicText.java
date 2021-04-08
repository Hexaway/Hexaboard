package net.hexaway.board.animation;

import net.hexaway.board.tick.TickableCounter;

public final class DynamicText {

    private String[] textLines;

    private TickableCounter tickableCounter;

    private int index = 0;

    public DynamicText(int updateTime, String... textLines) {
        this.textLines = textLines;
        this.tickableCounter = new TickableCounter(updateTime);
    }

    DynamicText() {

    }

    public String getNextLine() {
        if (tickableCounter.hasFinished()) {
            tickableCounter.restart();
            index += 1;
        }

        if (index >= textLines.length)
            index = 0;

        return textLines[index];
    }

    public TickableCounter getTickableCounter() {
        return tickableCounter;
    }
}
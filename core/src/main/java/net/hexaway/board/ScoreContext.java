package net.hexaway.board;

import net.hexaway.board.abstraction.LineHandler;

public class ScoreContext implements LineHandler.Context {

    private final String entry;
    private final String lastEntry;

    public ScoreContext(String entry, String lastEntry) {
        this.entry = entry;
        this.lastEntry = lastEntry;
    }

    @Override
    public String getEntry() {
        return entry;
    }

    public String getLastEntry() {
        return lastEntry;
    }
}
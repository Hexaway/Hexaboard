package net.hexaway.board;

import net.hexaway.board.abstraction.LineHandler;

public class TeamContext implements LineHandler.Context {

    private final String name;
    private final String entry;
    private final String prefix;
    private final String suffix;

    public TeamContext(String name, String entry, String prefix, String suffix) {
        this.name = name;
        this.entry = entry;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public String getEntry() {
        return entry;
    }

    @Override
    public String fullText() {
        return prefix + suffix;
    }
}
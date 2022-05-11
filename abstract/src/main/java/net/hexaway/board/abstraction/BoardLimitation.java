package net.hexaway.board.abstraction;

public final class BoardLimitation {

    private final int maxTitleChars;
    private final int maxScoreChars;
    private final int maxTeamPartChars;

    public BoardLimitation(int maxTitleChars, int maxScoreChars, int maxTeamPartChars) {
        this.maxTitleChars = maxTitleChars;
        this.maxScoreChars = maxScoreChars;
        this.maxTeamPartChars = maxTeamPartChars;
    }

    public int getMaxTitleChars() {
        return maxTitleChars;
    }

    public int getMaxScoreChars() {
        return maxScoreChars;
    }

    public int getMaxTeamPartChars() {
        return maxTeamPartChars;
    }

    public static BoardLimitation get1_8Schema() {
        return new BoardLimitation(32, 40, 16);
    }

    public static BoardLimitation get1_13Schema() {
        return new BoardLimitation(128, 40, 64);
    }

    public static BoardLimitation get1_18Schema() {
        return new BoardLimitation(128, 32767, 64);
    }
}
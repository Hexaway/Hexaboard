package net.hexaway.board;

public interface StringCutter {

    static String getPrefix(String text, int maxTeamPartChars) {
        return cut(0, maxTeamPartChars, text, text);
    }

    static String getSuffix(String text, int maxTeamPartChars) {
        return cut(maxTeamPartChars, maxTeamPartChars * 2, text, "");
    }

    static String cut(int from, int to, String text, String def) {
        if (text.length() <= from) return def;

        if (text.length() < to)
            to = text.length();

        return text.substring(from, to);
    }
}
package net.hexaway.board.util;

public class Validate {

    public static <T> T notNull(T o, String message) {
        if (o == null)
            throw new NullPointerException(message);

        return o;
    }

    public static void isTrue(boolean b, String message) {
        if (!b)
            throw new IllegalArgumentException(message);
    }
}
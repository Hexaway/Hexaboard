package net.hexaway.board.util;

public class Validate {

    public static void notNull(Object o, String message) {
        if (o == null)
            throw new NullPointerException(message);
    }

    public static void isTrue(boolean b, String message) {
        if (!b)
            throw new IllegalArgumentException(message);
    }
}
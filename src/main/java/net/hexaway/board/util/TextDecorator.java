package net.hexaway.board.util;

import org.bukkit.ChatColor;

public final class TextDecorator {

    public static String replaceColorChar(String text) {
        return text.replace(ChatColor.COLOR_CHAR + "", "&");
    }

    public static String[] replaceColorChar(String... text) {
        int length = text.length;

        for (int i = 0; i < length; i++) {
            text[i] = replaceColorChar(text[i]);
        }

        return text;
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String[] color(String[] text) {
        int length = text.length;
        for (int i = 0; i < length; i++) {
            text[i] = color(text[i]);
        }
        return text;
    }
}
package net.hexaway.board.util;

import com.google.gson.reflect.TypeToken;

import java.util.List;

public final class ArrayUtils {

    private static final TypeToken<List<String>> LIST_TYPE_TOKEN = new TypeToken<List<String>>() {
    };

    public static String[] toArray(Object o) throws IllegalArgumentException {
        if (LIST_TYPE_TOKEN.getRawType().isAssignableFrom(o.getClass())) {
            return ((List<String>) o).toArray(new String[0]);
        } else if (o instanceof String) {
            return new String[]{(String) o};
        } else if (o instanceof String[]) {
            return (String[]) o;
        }

        throw new IllegalArgumentException();
    }
}
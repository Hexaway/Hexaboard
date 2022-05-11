package net.hexaway.board.abstraction.util;

public class NonOverWritableReference<V> {

    private V value;

    public NonOverWritableReference() {
        this(null);
    }

    public NonOverWritableReference(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public synchronized void setValue(V value) {
        if (this.value != null)
            throw new UnsupportedOperationException("Cannot overwrite value");

        this.value = value;
    }
}
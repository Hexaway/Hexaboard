package net.hexaway.board.abstraction.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueSnapshot<E> {

    private final List<E> elements;
    private AtomicInteger index;

    public QueueSnapshot(List<E> elements) {
        this.elements = elements;
    }

    public E previous() {
        if (index.get() - 1 < 0)
            throw new IndexOutOfBoundsException((index.get() - 1) + " < 0");

        return elements.get(index.decrementAndGet());
    }

    public E current() {
        return elements.get(index.get());
    }

    public E peekNext() {
        if (index.get() >= elements.size() - 1)
            throw new IndexOutOfBoundsException(index.get() + " >= " + elements.size());

        return elements.get(index.getAndDecrement());
    }

    public List<E> getElements() {
        return elements;
    }

    public int getIndex() {
        return index.get();
    }
}
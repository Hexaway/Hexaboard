package net.hexaway.board.abstraction.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ResettableQueueImpl<E> implements ResettableQueue<E> {

    private final List<E> elements;
    private final AtomicInteger index = new AtomicInteger();

    public ResettableQueueImpl(List<E> elements) {
        this.elements = new LinkedList<>(elements);
    }

    @Override
    public QueueSnapshot<E> getSnapshot() {
        return new QueueSnapshot<>(new ArrayList<>(elements));
    }

    @Override
    public void add(E toAdd) {
        elements.add(toAdd);
    }

    @Override
    public E previous() {
        if (index.get() > 0)
           return elements.get(index.decrementAndGet());

        index.set(limit() - 1);
        return current();
    }

    @Override
    public E current() {
        return elements.get(index.get());
    }

    @Override
    public E peek() {
        if (index.incrementAndGet() < elements.size())
            return elements.get(index.get());

        index.set(0);
        return current();
    }

    @Override
    public boolean isLast() {
        return index.get() >= elements.size() - 1;
    }

    @Override
    public int currentIndex() {
        return index.get();
    }

    @Override
    public int limit() {
        return elements.size();
    }
}
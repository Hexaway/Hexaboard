package net.hexaway.board.abstraction.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface ResettableQueue<E> {

    QueueSnapshot<E> getSnapshot();

    void add(E toAdd);

    E previous();

    E current();

    E peek();

    boolean isLast();

    int currentIndex();

    int limit();

    @SafeVarargs
    static <E> ResettableQueue<E> of(E... elements) {
        return new ResettableQueueImpl<>(Arrays.asList(elements));
    }

    static <E> ResettableQueue<E> of(List<E> list) {
        return new ResettableQueueImpl<>(Objects.requireNonNull(list));
    }

    static <E> ResettableQueue<E> create() {
        return new ResettableQueueImpl<>(Collections.emptyList());
    }
}
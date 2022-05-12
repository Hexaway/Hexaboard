package net.hexaway.board.abstraction;

import net.hexaway.board.abstraction.util.QueueSnapshot;
import net.hexaway.board.abstraction.util.ResettableQueue;
import net.hexaway.board.abstraction.util.TickCounter;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class SimpleScoreboardElement implements ScoreboardElement {

    private final ResettableQueue<ElementFrame> queue;
    private final Map<String, Integer> contentMap;
    private TickCounter currentTickCounter;

    public SimpleScoreboardElement(Map<String, Integer> contentMap) {
        this.queue = ResettableQueue.create();
        contentMap.forEach((k, v) -> {
            if (v <= 0)
                throw new IllegalArgumentException("duration <= 0");

            Objects.requireNonNull(k, "frame text is null");
            queue.add(new ElementFrame(k, v));
        });

        this.contentMap = Collections.unmodifiableMap(contentMap);
        this.currentTickCounter = new TickCounter(queue.current().getDuration());
    }

    @Override
    public Map<String, Integer> getData() {
        return contentMap;
    }

    @Override
    public QueueSnapshot<ElementFrame> getQueueSnapshot() {
        return queue.getSnapshot();
    }

    @Override
    public ElementFrame current() {
        return queue.current();
    }

    @Override
    public String getCurrentFrameContent() {
        return queue.current().getContent();
    }

    @Override
    public TickCounter getCurrentWait() {
        return currentTickCounter;
    }

    @Override
    public boolean update() {
        currentTickCounter.elapse();

        if (currentTickCounter.isFinished()) {
            ElementFrame entry = queue.peek();
            this.currentTickCounter = new TickCounter(entry.getDuration());
            return true;
        }

        return false;
    }

    @Override
    public ScoreboardElement copy() {
        return new SimpleScoreboardElement(contentMap);
    }
}
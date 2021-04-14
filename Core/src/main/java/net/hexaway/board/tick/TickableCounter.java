package net.hexaway.board.tick;

public final class TickableCounter {

    private int totalTicks;

    private int currentTicks;

    public TickableCounter(int totalTicks) {
        this.totalTicks = totalTicks;
    }

    TickableCounter() {
    }

    public int getCurrentTicks() {
        return currentTicks;
    }

    public int getTotalTicks() {
        return totalTicks;
    }

    public boolean hasFinished() {
        return currentTicks >= totalTicks;
    }

    public void nextTick() {
        currentTicks++;
    }

    public void restart() {
        this.currentTicks = 0;
    }
}
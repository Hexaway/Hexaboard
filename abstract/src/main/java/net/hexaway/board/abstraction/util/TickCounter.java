package net.hexaway.board.abstraction.util;

public class TickCounter {

    private final int tickGoal;
    private int elapsedTicks;

    public TickCounter(int tickGoal) {
        this.tickGoal = tickGoal;
    }

    public boolean isFinished() {
        return elapsedTicks >= tickGoal;
    }

    public void elapse() {
        this.elapsedTicks+=1;
    }

    public int getTickGoal() {
        return tickGoal;
    }
}
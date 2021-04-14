package net.hexaway.board.scoreboard;

import org.bukkit.scoreboard.Objective;

import java.util.function.Consumer;

public final class ObjectiveSwapper {

    private Objective objective;

    private Objective buffer;

    public ObjectiveSwapper(Objective objective, Objective buffer) {
        this.objective = objective;
        this.buffer = buffer;
    }

    public Objective getShowing() {
        return objective;
    }

    public Objective getHidden() {
        return buffer;
    }

    public void swap() {
        Objective temp = buffer;

        this.buffer = objective;
        this.objective = temp;
    }

    public void swapAndRestore(Consumer<Objective> objectiveConsumer) {
        objectiveConsumer.accept(buffer);

        swap();

        objectiveConsumer.accept(buffer);

        swap();
    }
}
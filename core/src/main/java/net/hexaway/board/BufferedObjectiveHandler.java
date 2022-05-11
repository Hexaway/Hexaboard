package net.hexaway.board;

import net.hexaway.board.abstraction.InternalBoardHandler;
import net.hexaway.board.abstraction.ObjectiveHandler;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BufferedObjectiveHandler implements ObjectiveHandler {

    private ObjectiveHandler showed;
    private ObjectiveHandler hidden;

    public BufferedObjectiveHandler(String name, InternalBoardHandler internalBoardHandler, Supplier<Player> playerSupplier) {
        this.showed = new SimpleObjectiveHandler(name, name, internalBoardHandler, playerSupplier);
        this.hidden = new SimpleObjectiveHandler(name + "-2", name, internalBoardHandler, playerSupplier);
    }

    @Override
    public void create(String displayName) {
        this.showed.create(displayName);
        this.hidden.create(displayName);
    }

    @Override
    public void display() {
        showed.display();
    }

    @Override
    public void sendDisplayName(String displayName) {
        showed.sendDisplayName(displayName);
        hidden.sendDisplayName(displayName);
    }

    @Override
    public void sendScore(String entryName, int pos) {
        performAction(objectiveHandler -> objectiveHandler.sendScore(entryName, pos));
    }

    @Override
    public void removeScore(String entryName, int pos) {
        performAction(objectiveHandler -> objectiveHandler.removeScore(entryName, pos));
    }

    @Override
    public void updateScore(String entryName, String toRemove, int pos) {
        performAction(objectiveHandler -> objectiveHandler.updateScore(entryName, toRemove, pos));
    }

    @Override
    public void destroy() {
        hidden.destroy();
        showed.destroy();
    }

    private void performAction(Consumer<ObjectiveHandler> consumer) {
        ObjectiveHandler temp = showed;

        consumer.accept(hidden);

        hidden.display();
        showed = hidden;

        hidden = temp;

        consumer.accept(hidden);
    }
}
package net.hexaway.board;

import net.hexaway.board.abstraction.InternalBoardHandler;
import net.hexaway.board.abstraction.ObjectiveHandler;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public class SimpleObjectiveHandler implements ObjectiveHandler {

    private final InternalBoardHandler internalBoardHandler;
    private final Supplier<Player> playerSupplier;
    private final String name;

    public SimpleObjectiveHandler(String name, String displayName, InternalBoardHandler internalBoardHandler, Supplier<Player> playerSupplier) {
        this.internalBoardHandler = internalBoardHandler;
        this.playerSupplier = playerSupplier;
        this.name = name;

        create(displayName);
    }

    @Override
    public void create(String displayName) {
        internalBoardHandler.createObjective(name, displayName, playerSupplier.get());
    }

    @Override
    public void display() {
        internalBoardHandler.displayObjective(name, playerSupplier.get());
    }

    @Override
    public void sendDisplayName(String displayName) {
        internalBoardHandler.updateObjective(name, displayName, playerSupplier.get());
    }

    @Override
    public void sendScore(String entryName, int pos) {
        internalBoardHandler.createOrUpdateScore(entryName, name, pos, playerSupplier.get());
    }

    @Override
    public void removeScore(String entryName, int pos) {
        internalBoardHandler.removeScore(entryName, name, playerSupplier.get());
    }

    @Override
    public void updateScore(String entryName, String toRemove, int pos) {
        removeScore(toRemove, pos);
        sendScore(entryName, pos);
    }

    @Override
    public void destroy() {
        internalBoardHandler.removeObjective(name, playerSupplier.get());
    }
}
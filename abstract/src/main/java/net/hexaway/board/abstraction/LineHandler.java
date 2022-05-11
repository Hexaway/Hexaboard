package net.hexaway.board.abstraction;

import org.bukkit.entity.Player;

public interface LineHandler {

    void setLine(
            Context context,
            int position,
            boolean onlyUpdate,
            InternalBoardHandler internalBoardHandler,
            ObjectiveHandler objectiveHandler,
            Player player
    );

    interface Context {
        String getEntry();

        default String fullText() {
            return getEntry();
        }
    }
}
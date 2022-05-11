package net.hexaway.board;

import net.hexaway.board.abstraction.InternalBoardHandler;
import net.hexaway.board.abstraction.LineHandler;
import net.hexaway.board.abstraction.ObjectiveHandler;
import org.bukkit.entity.Player;

public class ScoreLineHandler implements LineHandler {

    @Override
    public void setLine(
            Context context,
            int position,
            boolean onlyUpdate,
            InternalBoardHandler internalBoardHandler,
            ObjectiveHandler objectiveHandler,
            Player player
    ) {
        if (!(context instanceof ScoreContext)) throw new IllegalArgumentException("Invalid context");

        ScoreContext scoreContext = (ScoreContext) context;
        String toRemove = scoreContext.getLastEntry();

        if (onlyUpdate && toRemove != null) {
            objectiveHandler.updateScore(context.getEntry(), toRemove, position);
        } else {
            objectiveHandler.sendScore(context.getEntry(), position);
        }
    }
}
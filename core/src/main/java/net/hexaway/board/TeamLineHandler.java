package net.hexaway.board;

import net.hexaway.board.abstraction.InternalBoardHandler;
import net.hexaway.board.abstraction.LineHandler;
import net.hexaway.board.abstraction.ObjectiveHandler;
import org.bukkit.entity.Player;

import java.util.Collections;

public class TeamLineHandler implements LineHandler {

    @Override
    public void setLine(Context context, int position, boolean onlyUpdate, InternalBoardHandler internalBoardHandler, ObjectiveHandler objectiveHandler, Player player) {
        if (!(context instanceof TeamContext)) throw new IllegalArgumentException("Invalid context");

        TeamContext teamLine = (TeamContext) context;

        if (onlyUpdate) {
            internalBoardHandler.updateTeam(
                    teamLine.getName(),
                    teamLine.getName(),
                    teamLine.getPrefix(),
                    teamLine.getSuffix(),
                    Collections.singletonList(teamLine.getEntry()),
                    player
            );
        } else {
            internalBoardHandler.createTeam(
                    teamLine.getName(),
                    teamLine.getName(),
                    teamLine.getPrefix(),
                    teamLine.getSuffix(),
                    Collections.singletonList(teamLine.getEntry()),
                    player
            );

            objectiveHandler.sendScore(teamLine.getEntry(), position);
        }
    }
}
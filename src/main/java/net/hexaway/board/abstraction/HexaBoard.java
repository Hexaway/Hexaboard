package net.hexaway.board.abstraction;

import net.hexaway.board.model.ScoreboardModel;

import java.util.Collection;

public interface HexaBoard {

    void setTitle(Title title);

    Title getTitle();

    void setLine(int pos, String... text);

    void setLine(ScoreboardLine scoreboardLine);

    void setLines(ScoreboardLine... lines);

    Collection<ScoreboardLine> getLines();

    ScoreboardModel getModel();

    void removeLine(int pos);

    void clearLines();

    void setScoreboard();

    void delete();

    void update();
}
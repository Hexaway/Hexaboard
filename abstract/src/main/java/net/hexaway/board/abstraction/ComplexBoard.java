package net.hexaway.board.abstraction;

import java.util.Map;

public interface ComplexBoard extends SimpleBoard {

    ScoreboardElement setComplexTitle(ScoreboardElement element);

    ScoreboardElement getComplexTitle();

    ScoreboardElement setComplexLine(int pos, ScoreboardElement line);

    ScoreboardElement getComplexLine(int pos);

    ScoreboardElement removeComplexLine(int pos);

    Map<Integer, ScoreboardElement> getComplexLines();

    void delete();

    boolean isDeleted();

    void tick();

}
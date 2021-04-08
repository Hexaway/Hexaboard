package net.hexaway.board.repository.serialization;

import net.hexaway.board.model.ScoreboardModel;

public abstract class ScoreboardModelSerializer<T> {

    public abstract T serializeBoard(ScoreboardModel scoreboardModel);

}
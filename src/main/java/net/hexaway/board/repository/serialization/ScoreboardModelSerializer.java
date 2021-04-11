package net.hexaway.board.repository.serialization;

import net.hexaway.board.model.ScoreboardModel;

public interface ScoreboardModelSerializer<T> {

    T serializeBoard(ScoreboardModel scoreboardModel);

}
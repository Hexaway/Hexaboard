package net.hexaway.board.repository.serialization;

import net.hexaway.board.model.ScoreboardModel;

public interface ScoreboardModelDeserializer<T> {

    ScoreboardModel deserializeBoard(T dataType) throws IllegalArgumentException;

}
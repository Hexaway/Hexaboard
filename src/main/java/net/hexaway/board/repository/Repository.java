package net.hexaway.board.repository;

import java.util.Set;

public interface Repository<T> {

    void save(String id, T object);

    void delete(String id);

    T find(String id);

    Set<T> findAll();
}
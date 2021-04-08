package net.hexaway.board.abstraction;

@FunctionalInterface
public interface Title {

    String get();

    default void update() {

    }
}
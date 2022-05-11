package net.hexaway.board.abstraction;

public interface ObjectiveHandler {

    void create(String displayName);

    void display();

    void sendDisplayName(String displayName);

    void sendScore(String entryName, int pos);

    void removeScore(String entryName, int pos);

    void updateScore(String entryName, String toRemove, int pos);

    void destroy();

}
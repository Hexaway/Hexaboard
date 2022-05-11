package net.hexaway.board.abstraction;

import org.bukkit.entity.Player;

import java.util.List;

public interface InternalBoardHandler {

    void createObjective(String name, String displayName, Player player);

    void updateObjective(String name, String displayName, Player player);

    void removeObjective(String name, Player player);

    void displayObjective(String name, Player player);

    void createOrUpdateScore(String entryName, String objectiveName, int score, Player player);

    void removeScore(String entryName, String objectiveName, Player player);

    void createTeam(
            String teamName,
            String displayName,
            String prefix,
            String suffix,
            List<String> playerNames,
            Player player
    );

    void updateTeam(
            String teamName,
            String displayName,
            String prefix,
            String suffix,
            List<String> playerNames,
            Player player
    );

    void removeTeam(String teamName, Player player);

}
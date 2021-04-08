package net.hexaway.board.nms;

import net.hexaway.board.scoreboard.ObjectiveSwapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class NMSScoreboardHelper {

    private final ObjectiveSwapper objectiveSwapper;

    private final Object nmsScoreboard;

    private final Map<String, Map> playerScores;

    private final UUID playerUUID;

    @SuppressWarnings("unchecked")
    public NMSScoreboardHelper(Scoreboard scoreboard, ObjectiveSwapper objectiveSwapper, Player player) throws IllegalAccessException, InvocationTargetException {
        this.objectiveSwapper = objectiveSwapper;
        this.nmsScoreboard = NMSScoreboardUtil.CRAFT_SCOREBOARD_HANDLE.invoke(scoreboard);
        this.playerScores = (Map<String, Map>) NMSScoreboardUtil.PLAYER_SCORES.get(nmsScoreboard);
        this.playerUUID = player.getUniqueId();
    }

    public void sendScore(String text, int pos, boolean remove) {
        Player player = getPlayer();

        if (player == null)
            return;

        try {
            if (remove) {
                removeScore(text);
                return;
            }

            Object score = createScore(text);

            set(score, pos);

            handleScores(objectiveSwapper.getHidden(), text, score, false);

            Object packet = NMSScoreboardUtil.SCOREBOARD_SCORE_PACKET_CHANGE
                    .newInstance(score);

            NMSUtil.sendPacket(player, packet);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error sending packet score", e);
        }
    }

    private void removeScore(String text) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Player player = getPlayer();

        if (player == null)
            return;

        handleScores(objectiveSwapper.getHidden(), text, null, true);

        Object packet = NMSScoreboardUtil.SCOREBOARD_SCORE_PACKET_REMOVE
                .newInstance(
                        text,
                        getObjectiveHandle(objectiveSwapper.getHidden())
                );

        NMSUtil.sendPacket(player, packet);
    }

    public void displayObjective(Objective objective) {
        Player player = getPlayer();

        if (player == null)
            return;

        try {
            Object packet = NMSScoreboardUtil.OBJECTIVE_DISPLAY_PACKET.newInstance(1, getObjectiveHandle(objective));

            NMSUtil.sendPacket(player, packet);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error displaying objective", e);
        }
    }

    public void sendObjectiveChange(Objective objective, int action) {
        Player player = getPlayer();

        if (player == null)
            return;

        try {
            Object packet = NMSScoreboardUtil.OBJECTIVE_SENDER_PACKET.newInstance(getObjectiveHandle(objective), action);

            NMSUtil.sendPacket(player, packet);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error sending objective", e);
        }
    }

    private Object getObjectiveHandle(Objective objective) {
        try {
            return NMSScoreboardUtil.OBJECTIVE_HANDLE.invoke(objective);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error invoking craft objective handle", e);
        }

        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void handleScores(Objective objective, String text, Object score, boolean remove) {
        Map map = playerScores.get(text);
        Object nmsObjective = getObjectiveHandle(objective);

        if (map == null) {
            map = new HashMap();
            playerScores.put(text, map);
        }

        if (remove) {
            map.remove(nmsObjective);
        } else {
            map.put(nmsObjective, score);
        }
    }

    private Object createScore(String text) {
        try {
            return NMSScoreboardUtil.NMS_SCOREBOARD_SCORE_CONSTRUCTOR
                    .newInstance(
                            nmsScoreboard,
                            getObjectiveHandle(objectiveSwapper.getHidden()),
                            text
                    );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error creating score", e);
        }

        return null;
    }

    private void set(Object scoreboardScore, int pos) {
        try {
            NMSScoreboardUtil.SET_SCORE_METHOD.invoke(scoreboardScore, pos);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }
}
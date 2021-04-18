package net.hexaway.board.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import net.hexaway.board.ScoreboardTeam;
import net.hexaway.board.abstraction.ScoreboardLine;
import net.hexaway.board.nms.NMSScoreboardHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * The function of this class is give a management of bukkit scoreboard
 */
public final class BukkitScoreboardHandler {

    private Scoreboard scoreboard;

    private Map<Integer, ScoreboardTeam> scores;

    private ObjectiveSwapper objectiveSwapper;

    private NMSScoreboardHelper nmsScoreboardHelper;

    private final UUID playerUUID;

    public BukkitScoreboardHandler(Scoreboard scoreboard, Player player, String title) {
        this.scoreboard = scoreboard;
        this.playerUUID = player.getUniqueId();

        this.scores = new HashMap<>();

        Objective objective = scoreboard.registerNewObjective("main", "dummy");
        objective.setDisplayName(title);

        Objective buffer = scoreboard.registerNewObjective("buffer", "dummy");
        buffer.setDisplayName(title);

        this.objectiveSwapper = new ObjectiveSwapper(objective, buffer);

        try {
            this.nmsScoreboardHelper = new NMSScoreboardHelper(scoreboard, objectiveSwapper, player);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error creating nms scoreboard helper", e);
        }
    }

    public BukkitScoreboardHandler(String title, Player player) {
        this(Bukkit.getScoreboardManager().getNewScoreboard(), player, title);
    }

    public void setTitle(String title) {
        if (checkHelper())
            return;

        Objective showing = objectiveSwapper.getShowing();
        Objective hidden = objectiveSwapper.getHidden();

        showing.setDisplayName(title);
        hidden.setDisplayName(title);

        nmsScoreboardHelper.sendObjectiveChange(showing, 2);
        nmsScoreboardHelper.sendObjectiveChange(hidden, 2);
    }

    public void setLine(boolean usePlaceholderAPI, ScoreboardLine line) {
        if (line == null)
            return;

        if (checkHelper())
            return;

        Player player = getPlayer();

        if (player == null)
            return;

        int pos = line.position();
        String fullText = usePlaceholderAPI ? PlaceholderAPI.setPlaceholders(player, line.get()) : line.get();
        ScoreboardTeam oldTeam = this.scores.get(pos);

        // returns if the current text equal to an old text in the same position
        if (oldTeam != null && fullText.equals(oldTeam.getFullText()))
            return;

        ScoreboardTeam scoreboardTeam = new ScoreboardTeam(fullText);

        updateScore(fullText, oldTeam, scoreboardTeam, pos);
    }

    public void removeScore(int pos) {
        ScoreboardTeam scoreboardTeam = scores.get(pos);

        if (scoreboardTeam != null) {
            scoreboard.resetScores(scoreboardTeam.get());
            scores.remove(pos, scoreboardTeam);
        }
    }

    public void delete() {
        Objective showing = objectiveSwapper.getShowing();
        Objective hidden = objectiveSwapper.getHidden();

        showing.unregister();
        hidden.unregister();

        nmsScoreboardHelper.sendObjectiveChange(showing, 1);
        nmsScoreboardHelper.sendObjectiveChange(hidden, 1);

        this.scoreboard = null;
        this.scores = null;
        this.nmsScoreboardHelper = null;
        this.objectiveSwapper = null;
    }

    public void sendScoreboard() {
        if (checkHelper())
            return;

        Player player = getPlayer();

        if (player == null)
            return;

        player.setScoreboard(scoreboard);

        nmsScoreboardHelper.sendObjectiveChange(objectiveSwapper.getShowing(), 0);
        nmsScoreboardHelper.displayObjective(objectiveSwapper.getShowing());

        nmsScoreboardHelper.sendObjectiveChange(objectiveSwapper.getHidden(), 0);
    }

    private void scoreAction(String oldText, String text, int pos, ScoreAction scoreAction) {
        switch (scoreAction) {
            case SEND:
                nmsScoreboardHelper.sendScore(text, pos, false);
                break;
            case REPLACE:
                scoreAction(oldText, null, pos, ScoreAction.REMOVE);
                scoreAction(null, text, pos, ScoreAction.SEND);
                break;
            case REMOVE:
                nmsScoreboardHelper.sendScore(oldText, pos, true);
                break;
            default:
                break;
        }
    }

    private void updateScore(String fullText, ScoreboardTeam oldTeam, ScoreboardTeam scoreboardTeam, int pos) {
        String teamText = scoreboardTeam.get();
        Team team = getTeam(teamText);

        if (oldTeam == null) {
            // if the score of the specified position does not have a text, only the scores will be sent to it, so that the buffer and the displayed objective update their data
            objectiveSwapper.swapAndRestore((objective) -> scoreAction(null, teamText, pos, ScoreAction.SEND));
        } else {
            // so if score of the specified position does have a text, the scores will be replaced (send text after removing old text)
            // update buffer
            scoreAction(oldTeam.get(), teamText, pos, ScoreAction.REPLACE);

            //to prevent flicking, display the buffer and swap objectives, the displayed objective is the buffer and the buffer is the displayed objective
            nmsScoreboardHelper.displayObjective(objectiveSwapper.getHidden());
            objectiveSwapper.swap();

            scoreAction(oldTeam.get(), teamText, pos, ScoreAction.REPLACE);
        }

        team.setPrefix(scoreboardTeam.getPrefix());
        team.setSuffix(scoreboardTeam.getSuffix());

        this.scores.put(pos, new ScoreboardTeam(fullText));
    }

    private Team getTeam(String teamText) {
        Team team = scoreboard.getTeam(teamText);

        if (team == null) {
            team = scoreboard.registerNewTeam(teamText);
        }

        if (!team.hasEntry(teamText)) {
            team.addEntry(teamText);
        }

        return team;
    }

    private boolean checkHelper() {
        return nmsScoreboardHelper == null;
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    private enum ScoreAction {
        SEND,
        REPLACE,
        REMOVE;
    }
}
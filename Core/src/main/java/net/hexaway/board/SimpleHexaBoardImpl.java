package net.hexaway.board;

import net.hexaway.board.abstraction.HexaBoard;
import net.hexaway.board.abstraction.ScoreboardLine;
import net.hexaway.board.abstraction.ScoreboardManager;
import net.hexaway.board.abstraction.Title;
import net.hexaway.board.animation.DynamicScoreboardLine;
import net.hexaway.board.model.ScoreboardModel;
import net.hexaway.board.scoreboard.BukkitScoreboardHandler;
import net.hexaway.board.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class SimpleHexaBoardImpl implements HexaBoard {

    private ScoreboardModel model;

    protected final BukkitScoreboardHandler bukkitScoreboardHandler;

    private ScoreboardManager scoreboardManager;

    private final Map<Integer, ScoreboardLine> lines;

    private Title title;

    private final UUID uuid;

    private boolean displayed;

    private boolean deleted;

    public SimpleHexaBoardImpl(ScoreboardModel scoreboardModel, ScoreboardManager scoreboardManager, Player player) {
        Validate.notNull(scoreboardModel, "scoreboardModel");
        Validate.notNull(scoreboardManager, "scoreboardManager");
        Validate.notNull(player, "player");

        this.model = scoreboardModel;
        this.title = scoreboardModel.getTitle().newTitle();
        this.uuid = player.getUniqueId();

        this.lines = new HashMap<>();
        this.bukkitScoreboardHandler = new BukkitScoreboardHandler(this.title.get(), player);
        this.scoreboardManager = scoreboardManager;

        scoreboardModel.getScoreboardLines().forEach(scoreboardLine -> lines.put(scoreboardLine.position(), scoreboardLine));
    }

    public SimpleHexaBoardImpl(Player player) {
        Validate.notNull(player, "player");

        this.uuid = player.getUniqueId();

        this.lines = new HashMap<>();
        this.bukkitScoreboardHandler = new BukkitScoreboardHandler("title", player);
    }

    @Override
    public void setTitle(Title title) {
        checkPlayerStatus();

        if (deleted)
            return;

        Validate.notNull(title, "title");

        this.title = title;
    }

    @Override
    public Title getTitle() {
        return title;
    }

    @Override
    public void setLine(int pos, String... text) {
        set(new DynamicScoreboardLine(pos, 0, text));
    }

    @Override
    public void setLine(ScoreboardLine scoreboardLine) {
        Validate.notNull(scoreboardLine, "scoreboard line cannot be null");

        set(scoreboardLine);
    }

    @Override
    public ScoreboardModel getModel() {
        return model;
    }

    @Override
    public Collection<ScoreboardLine> getLines() {
        return Collections.unmodifiableCollection(lines.values());
    }

    @Override
    public void removeLine(int pos) {
        checkPlayerStatus();

        if (deleted)
            return;

        ScoreboardLine scoreboardLine = lines.get(pos);

        if (scoreboardLine == null || scoreboardLine.position() != pos)
            return;

        lines.remove(pos, scoreboardLine);
        bukkitScoreboardHandler.removeScore(pos);
    }

    @Override
    public void clearLines() {
        new HashSet<>(lines.keySet()).forEach(this::removeLine);
    }

    @Override
    public void setLines(ScoreboardLine... lines) {
        Validate.notNull(lines, "scoreboard lines cannot be null");

        List<ScoreboardLine> lineList = Arrays.asList(lines);

        if (lineList.isEmpty())
            return;

        for (ScoreboardLine scoreboardLine : lineList) {
            if (scoreboardLine == null)
                continue;

            set(scoreboardLine);
        }
    }

    @Override
    public void setScoreboard() {
        if (!displayed && !deleted) {
            bukkitScoreboardHandler.sendScoreboard();
            displayed = true;
        }
    }

    @Override
    public void delete() {
        if (!deleted) {
            bukkitScoreboardHandler.delete();

            if (scoreboardManager != null)
                scoreboardManager.unregisterScoreboard(uuid);

            this.deleted = true;
        }
    }

    @Override
    public void update() {
        checkPlayerStatus();

        if (deleted)
            return;

        title.update();

        Set<ScoreboardLine> lines = new HashSet<>(this.lines.values());
        lines.forEach(ScoreboardLine::update);

        bukkitScoreboardHandler.setLines(usePlaceholderAPI(), lines.toArray(new ScoreboardLine[0]));

        bukkitScoreboardHandler.setTitle(title.get());
    }

    private void set(ScoreboardLine scoreboardLine) {
        checkPlayerStatus();

        if (deleted)
            return;

        int pos = scoreboardLine.position();

        Validate.notNull(scoreboardLine.get(), "line text cannot be null");
        Validate.isTrue(pos > -1 && pos < 15, "out of range (pos=" + pos + "/15)");

        this.lines.put(pos, scoreboardLine);
    }

    private void checkPlayerStatus() {
        Player player = Bukkit.getPlayer(uuid);

        if ((player == null || !player.isOnline()) && !deleted) {
            delete();
        }
    }

    private boolean usePlaceholderAPI() {
        return scoreboardManager != null && scoreboardManager.usePlaceholderAPI();
    }
}
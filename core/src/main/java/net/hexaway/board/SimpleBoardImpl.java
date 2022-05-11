package net.hexaway.board;

import net.hexaway.board.abstraction.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

public class SimpleBoardImpl implements SimpleBoard {

    private static final int MAX_SCORE_SIZE = 15;
    private static final String[] INVISIBLE_NAMES = new String[MAX_SCORE_SIZE];

    static {
        ChatColor[] colors = ChatColor.values();

        for (int i = 0; i < INVISIBLE_NAMES.length; i++) {
            INVISIBLE_NAMES[i] = colors[i].toString() + ChatColor.RESET;
        }
    }

    private static final AtomicInteger BOARD_COUNTER = new AtomicInteger();

    private final LineHandler.Context[] contexts = new LineHandler.Context[MAX_SCORE_SIZE];
    private final FrameInterceptor frameInterceptor;
    private final boolean useTeams;

    protected final UUID uuid;
    protected final InternalBoardHandler internalBoardHandler;
    protected final String name;
    protected final ObjectiveHandler objectiveHandler;
    protected final LineHandler lineHandler;
    protected final BoardLimitation limitation;

    private boolean deleted;
    private String title;

    public SimpleBoardImpl(
            FrameInterceptor frameInterceptor,
            LineAlgorithm lineAlgorithm,
            Player player,
            InternalBoardHandler internalBoardHandler,
            BoardLimitation limitation
    ) {
        this.frameInterceptor = frameInterceptor;

        int maxCharsPerLine = limitation.getMaxTeamPartChars() * 2; // max line length using teams

        boolean useTeams = lineAlgorithm == LineAlgorithm.TEAMS ||
                (lineAlgorithm == LineAlgorithm.ANY && maxCharsPerLine >= limitation.getMaxScoreChars());

        this.useTeams = useTeams;
        this.uuid = player.getUniqueId();
        this.internalBoardHandler = requireNonNull(internalBoardHandler);
        this.name = "#" + BOARD_COUNTER.incrementAndGet();
        this.title = name;

        if (useTeams) {
            this.objectiveHandler = new SimpleObjectiveHandler(this.name, name, internalBoardHandler, this::internalPlayerGet);
            this.lineHandler = new TeamLineHandler();
        } else {
            this.objectiveHandler = new BufferedObjectiveHandler(this.name, internalBoardHandler, this::internalPlayerGet);
            this.lineHandler = new ScoreLineHandler();
        }

        this.limitation = requireNonNull(limitation);
        objectiveHandler.display();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setTitle(String title) {
        checkState();

        Player player = internalPlayerGet();

        if (frameInterceptor != null) {
            String intercepted = frameInterceptor.interceptTitleFrame(title, player);

            if (intercepted != null) {
                if (intercepted.length() > limitation.getMaxTitleChars())
                    intercepted = StringCutter.cut(0, limitation.getMaxTitleChars(), intercepted, null);

                title = intercepted;
            }
        }

        if (this.title.equals(title))
            return;

        this.title = requireNonNull(title);
        objectiveHandler.sendDisplayName(title);
    }

    @Override
    public String getShowingTitle() {
        return title;
    }

    @Override
    public void setLines(List<String> lines) {
        int i = Math.min(lines.size(), MAX_SCORE_SIZE);

        for (String line : lines) {
            if (i <= 0)
                return;

            setLine(i, line);
            i--;
        }
    }

    @Override
    public void setLine(int pos, String text) {
        checkState();

        if (pos < 0 || pos > MAX_SCORE_SIZE) {
            throw new IndexOutOfBoundsException("pos < 0 or pos > " + MAX_SCORE_SIZE);
        }

        Player player = internalPlayerGet();

        text = normalize(text);

        LineHandler.Context oldContext = contexts[pos];

        if (frameInterceptor != null) {
            String intercepted = frameInterceptor().interceptLineFrame(text, player);
            if (intercepted != null) text = intercepted;
        }

        String oldText = oldContext != null ? oldContext.fullText() : null;

        if (oldContext != null && text.equals(oldText))
            return;

        String entry;
        LineHandler.Context context;

        if (useTeams) {
            entry = INVISIBLE_NAMES[pos];

            context = newTeamContext(entry, text);
        } else {
            context = new ScoreContext(text, oldText);
        }

        contexts[pos] = context;
        lineHandler.setLine(context, pos, oldText != null, internalBoardHandler, objectiveHandler, player);
    }

    @Override
    public LineHandler.Context getLine(int pos) {
        checkState();

        return this.contexts[pos];
    }

    @Override
    public void removeLine(int pos) {
        checkState();

        Player player = internalPlayerGet();
        LineHandler.Context context = contexts[pos];

        if (context != null) {
            internalBoardHandler.removeScore(context.getEntry(), name, player);
            contexts[pos] = null;
        }
    }

    @Override
    public List<LineHandler.Context> getShowingLines() {
        return new ArrayList<>(Arrays.asList(contexts));
    }

    @Override
    public FrameInterceptor frameInterceptor() {
        return frameInterceptor;
    }

    @Override
    public void clearShowingLines() {
        checkState();

        Player player = internalPlayerGet();
        for (int i = 0; i < contexts.length; i++) {
            contexts[i] = null;

            internalBoardHandler.removeScore(INVISIBLE_NAMES[i], name, player);
        }
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void delete() {
        checkState();

        Player player = getPlayer();

        if (player != null) {
            if (useTeams) {
                for (String entryName : INVISIBLE_NAMES) {
                    internalBoardHandler.removeTeam(entryName, player);
                }
            }

            clearShowingLines();
            internalBoardHandler.removeObjective(name, player);
        }

        Arrays.fill(contexts, null);
        title = null;
        deleted = true;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void reShow() {
        checkState();

        objectiveHandler.create(title);
        objectiveHandler.display();

        LineHandler.Context[] copy = Arrays.copyOf(contexts, contexts.length);

        Arrays.fill(contexts, null);
        for (int i = 0; i < copy.length; i++) {
            LineHandler.Context context = copy[i];

            if (context == null)
                continue;

            setLine(i, context.fullText());
        }
    }

    private Player internalPlayerGet() {
        Player player = getPlayer();

        if (player == null)
            throw new IllegalStateException("Player reference was discarded");

        return player;
    }

    protected void checkState() {
        if (deleted) throw new IllegalStateException("Board is deleted");
    }

    private String normalize(String text) {
        int maxLineChars;

        if (useTeams) {
            maxLineChars = limitation.getMaxTeamPartChars() * 2;
        } else {
            maxLineChars = limitation.getMaxScoreChars();
        }

        if (text.length() > maxLineChars) {
            text = text.substring(0, maxLineChars);
        }

        if (text.endsWith("§"))
            text = text.substring(0, text.length() - 1);

        return text;
    }

    private TeamContext newTeamContext(String entry, String text) {
        String prefix = StringCutter.getPrefix(text, limitation.getMaxTeamPartChars());
        String suffix = StringCutter.getSuffix(text, limitation.getMaxTeamPartChars());

        StringBuilder prefixBuilder = new StringBuilder(prefix);
        StringBuilder suffixBuilder = new StringBuilder(suffix);

        if (prefix.endsWith("§")) {
            prefixBuilder.deleteCharAt(prefix.length() - 1);
            suffixBuilder.insert(0, "§");

            if (suffixBuilder.length() > limitation.getMaxTeamPartChars()) {
                suffixBuilder.deleteCharAt(suffixBuilder.length() - 1);
            }
        }

        int suffixLength = suffixBuilder.length();
        if (suffixLength > 1) {
            int i = suffixLength - 1;
            char lastChar = suffixBuilder.charAt(i);

            if (lastChar == '§') {
                suffixBuilder.deleteCharAt(i);
            }
        }

        prefix = prefixBuilder.toString();
        suffix = suffixBuilder.toString();

        return new TeamContext(
                entry,
                entry,
                prefix,
                suffix
        );
    }
}
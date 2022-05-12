package net.hexaway.board;

import net.hexaway.board.abstraction.*;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class SimpleComplexBoard extends SimpleBoardImpl implements ComplexBoard {

    private final UUID uuid;
    private final Map<Integer, ScoreboardElement> complexLines = new ConcurrentHashMap<>();
    private final ComplexBoardManager boardManager;

    private ScoreboardElement title;

    public SimpleComplexBoard(
            Player player,
            InternalBoardHandler internalBoardHandler,
            LineAlgorithm lineAlgorithm,
            FrameInterceptor frameInterceptor,
            BoardLimitation limitation,
            ComplexBoardManager boardManager
    ) {
        super(frameInterceptor, lineAlgorithm, player, internalBoardHandler, limitation);
        this.uuid = player.getUniqueId();
        this.boardManager = boardManager;
        this.title = ScoreboardElement.simple(getShowingTitle());
    }

    @Override
    public ScoreboardElement setComplexTitle(ScoreboardElement element) {
        ScoreboardElement temp = title;

        this.title = requireNonNull(element);
        setTitle(title.getCurrentFrameContent());

        return temp;
    }

    @Override
    public ScoreboardElement getComplexTitle() {
        return title;
    }

    @Override
    public ScoreboardElement setComplexLine(int pos, ScoreboardElement line) {
        line = line.copy();

        ScoreboardElement prev = complexLines.put(pos, line);

        if (prev != null) {
            removeLine(pos);
        }

        setLine(pos, line.getCurrentFrameContent());

        return prev;
    }

    @Override
    public ScoreboardElement getComplexLine(int pos) {
        return complexLines.get(pos);
    }

    @Override
    public ScoreboardElement removeComplexLine(int pos) {
        ScoreboardElement prev = complexLines.remove(pos);

        if (prev != null) {
            removeLine(pos);
        }

        return prev;
    }

    @Override
    public Map<Integer, ScoreboardElement> getComplexLines() {
        return complexLines;
    }

    @Override
    public void delete() {
        super.delete();

        this.complexLines.clear();
        this.title = null;

        boardManager.unregisterBoard(uuid);
    }

    @Override
    public void tick() {
        if (isDeleted())
            throw new IllegalStateException("Board is deleted");

        if (title.update()) {
            setTitle(title.getCurrentFrameContent());
        }

        complexLines.forEach((k, v) -> {
            if (v.update()) {
                setLine(k, v.getCurrentFrameContent());
            }
        });
    }
}
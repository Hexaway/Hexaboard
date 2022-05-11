package net.hexaway.board;

import net.hexaway.board.abstraction.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.*;

import static java.util.Objects.requireNonNull;

public class SimpleComplexBoardManager implements ComplexBoardManager {

    private static final ScheduledExecutorService BOARD_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    private final Map<UUID, ComplexBoard> scoreboardMap = new ConcurrentHashMap<>();
    private final InternalBoardHandler internalBoardHandler;
    private final LimitationProvider limitationProvider;

    private ScheduledFuture<?> updaterTask;

    public SimpleComplexBoardManager(
            InternalBoardHandler internalBoardHandler,
            LimitationProvider limitationProvider
    ) {
        this.internalBoardHandler = requireNonNull(internalBoardHandler);
        this.limitationProvider = requireNonNull(limitationProvider);

        start();
    }

    public SimpleComplexBoardManager(InternalBoardHandler internalBoardHandler) {
        this(internalBoardHandler, new ServerLimitationProvider());
    }

    public SimpleComplexBoardManager() {
        this(InternalBoardHandlerProvider.get());
    }

    @Override
    public ComplexBoard createBoard(Player player, LineAlgorithm lineAlgorithm, FrameInterceptor frameInterceptor) {
        requireNonNull(player, "player");

        ComplexBoard old = this.scoreboardMap.get(player.getUniqueId());

        if (old != null) {
            old.delete();
        }

        BoardLimitation limitation = limitationProvider.getProtocolSchema(player);
        ComplexBoard complexBoard = new SimpleComplexBoard(player, internalBoardHandler, lineAlgorithm, frameInterceptor, limitation, this);

        this.scoreboardMap.put(player.getUniqueId(), complexBoard);
        return complexBoard;
    }

    @Override
    public void unregisterBoard(UUID uuid) {
        ComplexBoard complexBoard = scoreboardMap.remove(uuid);
        if (complexBoard != null && !complexBoard.isDeleted()) {
            complexBoard.delete();
        }
    }

    @Override
    public Optional<ComplexBoard> getBoard(Player player) {
        return Optional.ofNullable(scoreboardMap.get(player.getUniqueId()));
    }

    @Override
    public Map<UUID, ComplexBoard> getBoards() {
        return Collections.unmodifiableMap(scoreboardMap);
    }

    @Override
    public void stop() {
        if (updaterTask != null) {
            this.updaterTask.cancel(false);
            this.updaterTask = null;
        }
    }

    @Override
    public void resume() {
        if (updaterTask == null) {
            start();
        }
    }

    private void start() {
        this.updaterTask = BOARD_EXECUTOR_SERVICE.scheduleAtFixedRate(this::updateScoreboards, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void updateScoreboards() {
        Iterator<Map.Entry<UUID, ComplexBoard>> iterator = scoreboardMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, ComplexBoard> entry = iterator.next();

            ComplexBoard board = entry.getValue();

            if (board.getPlayer() == null) {
                continue;
            }

            try {
                board.tick();
            } catch (Exception e) {
                iterator.remove();
                throw new RuntimeException("Board '" + board.getName() + "' caused an unexpected exception", e);
            }
        }
    }
}
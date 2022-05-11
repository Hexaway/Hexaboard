package net.hexaway.board.abstraction;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ComplexBoardManager {

    ComplexBoard createBoard(Player player, LineAlgorithm lineAlgorithm, FrameInterceptor frameInterceptor);

    void unregisterBoard(UUID uuid);

    Optional<ComplexBoard> getBoard(Player player);

    Map<UUID, ComplexBoard> getBoards();

    void stop();

    void resume();

}

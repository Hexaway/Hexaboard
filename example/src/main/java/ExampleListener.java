import net.hexaway.board.abstraction.*;
import net.hexaway.board.model.ScoreboardPack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class ExampleListener implements Listener {

    private final ScoreboardPack pack;
    private final ComplexBoardManager complexBoardManager;
    private final FrameInterceptor frameInterceptor;

    public ExampleListener(ScoreboardPack pack, ComplexBoardManager complexBoardManager, FrameInterceptor frameInterceptor) {
        this.pack = pack;
        this.complexBoardManager = complexBoardManager;
        this.frameInterceptor = new ColorInterceptor(frameInterceptor);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Optional<ComplexBoard> boardOptional = complexBoardManager
                .getBoard(player);

        if (boardOptional.isPresent()) {
            ComplexBoard board = boardOptional.get();

            // If player disconnected and persist in manager use this to show again
            board.reShow();
        } else {
            // Use LineAlgorithm.ANY to use the algorithm that permits more chars per line
            ComplexBoard board = complexBoardManager.createBoard(player, LineAlgorithm.ANY, frameInterceptor);
            pack.install(board);
        }
    }
}
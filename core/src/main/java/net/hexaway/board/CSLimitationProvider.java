package net.hexaway.board;

import fr.onecraft.clientstats.ClientStats;
import net.hexaway.board.abstraction.BoardLimitation;
import net.hexaway.board.abstraction.LimitationProvider;
import org.bukkit.entity.Player;

public class CSLimitationProvider implements LimitationProvider {

    private final ServerLimitationProvider serverLimitationProvider = new ServerLimitationProvider();

    @Override
    public BoardLimitation getProtocolSchema(Player player) {
        int protocol = ClientStats.getApi().getProtocol(player.getUniqueId());

        if (protocol >= 758)
            return BoardLimitation.get1_18Schema();

        if (protocol >= 393) {
            return BoardLimitation.get1_13Schema();
        }

        if (protocol >= 47) {
            return BoardLimitation.get1_8Schema();
        }

        return serverLimitationProvider.getProtocolSchema(player);
    }
}
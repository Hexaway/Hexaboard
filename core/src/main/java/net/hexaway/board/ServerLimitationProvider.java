package net.hexaway.board;

import net.hexaway.board.abstraction.BoardLimitation;
import net.hexaway.board.abstraction.LimitationProvider;
import net.hexaway.board.abstraction.util.Version;
import org.bukkit.entity.Player;

public class ServerLimitationProvider implements LimitationProvider {
    @Override
    public BoardLimitation getProtocolSchema(Player player) {
        int v = Version.VERSION_MAJOR_NUMBER;

        if (v >= 18) {
            return BoardLimitation.get1_18Schema();
        } else if (v >= 13) {
            return BoardLimitation.get1_13Schema();
        } else {
            return BoardLimitation.get1_8Schema();
        }
    }
}
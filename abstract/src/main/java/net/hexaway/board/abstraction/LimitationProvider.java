package net.hexaway.board.abstraction;

import org.bukkit.entity.Player;

public interface LimitationProvider {

    BoardLimitation getProtocolSchema(Player player);

}
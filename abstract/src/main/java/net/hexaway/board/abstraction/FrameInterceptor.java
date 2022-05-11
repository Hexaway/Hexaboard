package net.hexaway.board.abstraction;

import org.bukkit.entity.Player;

public interface FrameInterceptor {

    String interceptLineFrame(String text, Player player);

    String interceptTitleFrame(String title, Player player);

}
package net.hexaway.board.abstraction;

import org.bukkit.entity.Player;

import java.util.List;

public interface SimpleBoard {

    String getName();

    void setTitle(String title);

    String getShowingTitle();

    void setLines(List<String> lines);

    void setLine(int pos, String line);

    LineHandler.Context getLine(int pos);

    void removeLine(int pos);

    List<LineHandler.Context> getShowingLines();

    FrameInterceptor frameInterceptor();

    void clearShowingLines();

    Player getPlayer();

    void delete();

    boolean isDeleted();

    void reShow();

}
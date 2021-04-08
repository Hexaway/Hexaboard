package net.hexaway.board.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NMSScoreboardUtil {

    public static Class<?> NMS_SCOREBOARD_CLASS;

    public static Class<?> NMS_OBJECTIVE_CLASS;

    public static Class<?> NMS_SCOREBOARD_SCORE_CLASS;

    public static Class<?> CB_SCOREBOARD_SCORE_CLASS;

    public static Constructor<?> NMS_SCOREBOARD_SCORE_CONSTRUCTOR;

    public static Constructor<?> SCOREBOARD_SCORE_PACKET_CHANGE;

    public static Constructor<?> SCOREBOARD_SCORE_PACKET_REMOVE;

    public static Constructor<?> OBJECTIVE_DISPLAY_PACKET;

    public static Constructor<?> OBJECTIVE_SENDER_PACKET;

    public static Field PLAYER_SCORES;

    public static Method CRAFT_SCOREBOARD_HANDLE;

    public static Method OBJECTIVE_HANDLE;

    public static Method SET_SCORE_METHOD;

    static {
        try {
            NMS_SCOREBOARD_CLASS = NMSUtil.getNMSClass("Scoreboard");
            NMS_OBJECTIVE_CLASS = NMSUtil.getNMSClass("ScoreboardObjective");
            NMS_SCOREBOARD_SCORE_CLASS = NMSUtil.getNMSClass("ScoreboardScore");
            CB_SCOREBOARD_SCORE_CLASS = NMSUtil.getCBClass("scoreboard.CraftScoreboard");

            NMS_SCOREBOARD_SCORE_CONSTRUCTOR = NMS_SCOREBOARD_SCORE_CLASS.getConstructor(NMS_SCOREBOARD_CLASS, NMS_OBJECTIVE_CLASS, String.class);

            Class<?> scoreboardScorePacketClass = NMSUtil.getNMSClass("PacketPlayOutScoreboardScore");
            SCOREBOARD_SCORE_PACKET_CHANGE = scoreboardScorePacketClass
                    .getConstructor(NMS_SCOREBOARD_SCORE_CLASS);
            SCOREBOARD_SCORE_PACKET_REMOVE = scoreboardScorePacketClass
                    .getConstructor(String.class, NMS_OBJECTIVE_CLASS);
            OBJECTIVE_DISPLAY_PACKET = NMSUtil.getNMSClass("PacketPlayOutScoreboardDisplayObjective")
                    .getConstructor(int.class, NMS_OBJECTIVE_CLASS);
            OBJECTIVE_SENDER_PACKET = NMSUtil.getNMSClass("PacketPlayOutScoreboardObjective")
                    .getConstructor(NMS_OBJECTIVE_CLASS, int.class);

            PLAYER_SCORES = NMS_SCOREBOARD_CLASS.getDeclaredField("playerScores");
            PLAYER_SCORES.setAccessible(true);

            CRAFT_SCOREBOARD_HANDLE = CB_SCOREBOARD_SCORE_CLASS.getMethod("getHandle");

            OBJECTIVE_HANDLE = NMSUtil.getCBClass("scoreboard.CraftObjective").getDeclaredMethod("getHandle");
            OBJECTIVE_HANDLE.setAccessible(true);

            SET_SCORE_METHOD = NMS_SCOREBOARD_SCORE_CLASS.getMethod("setScore", int.class);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
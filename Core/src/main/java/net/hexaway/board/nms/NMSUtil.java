package net.hexaway.board.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtil {

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private static Method getHandleMethod;

    private static Method sendPacketMethod;

    private static Field playerConnectionField;

    static {
        try {
            Class<?> craftPlayerClass = getCBClass("entity.CraftPlayer");
            Class<?> entityPlayerClass = getNMSClass("EntityPlayer");
            Class<?> playerConnectionClass = getNMSClass("PlayerConnection");
            sendPacketMethod = playerConnectionClass.getMethod("sendPacket", getNMSClass("Packet"));
            getHandleMethod = craftPlayerClass.getMethod("getHandle");
            playerConnectionField = entityPlayerClass.getField(("playerConnection"));
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + SERVER_VERSION + "." + name);
    }

    public static Class<?> getCBClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + "." + name);
    }

    public static Object getHandle(Class<?> clazz, Object instance) {
        try {
            Method method = clazz.getDeclaredMethod("getHandle");
            method.setAccessible(true);
            return method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object entityPlayer = getHandleMethod.invoke(player);
            Object playerConnection = playerConnectionField.get(entityPlayer);
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
package net.hexaway.board.abstraction.util;

import org.bukkit.Bukkit;

public class Version {

    public static final int VERSION_MAJOR_NUMBER;
    public static final String PACKAGE_NAME;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        String dominantVersion = version.substring(
                version.lastIndexOf("v") + 1, version.lastIndexOf("_"));

        PACKAGE_NAME = version.substring(version.lastIndexOf(".") + 1);
        VERSION_MAJOR_NUMBER = Integer.parseInt(dominantVersion.split("_")[1]);
    }
}
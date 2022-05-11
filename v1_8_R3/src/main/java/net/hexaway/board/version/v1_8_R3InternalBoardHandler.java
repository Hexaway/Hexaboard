package net.hexaway.board.version;

import io.netty.buffer.Unpooled;
import net.hexaway.board.abstraction.InternalBoardHandler;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class v1_8_R3InternalBoardHandler implements InternalBoardHandler {

    @Override
    public void createObjective(String name, String displayName, Player player) {
        sendObjectivePacket(name, displayName, 0, player);
    }

    @Override
    public void updateObjective(String name, String displayName, Player player) {
        sendObjectivePacket(name, displayName, 2, player);
    }

    @Override
    public void removeObjective(String name, Player player) {
        sendObjectivePacket(name, "", 1, player);
    }

    @Override
    public void displayObjective(String name, Player player) {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();

        PacketDataSerializer serializer = newSerializer();

        serializer.writeByte(1);
        serializer.a(name);

        write(packet, serializer);
        sendPacket(packet, player);
    }

    @Override
    public void createOrUpdateScore(String entryName, String objectiveName, int score, Player player) {
        sendScorePacket(entryName, objectiveName, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE, score, player);
    }

    @Override
    public void removeScore(String entryName, String objectiveName, Player player) {
        sendScorePacket(entryName, objectiveName, PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE, 0, player);
    }

    @Override
    public void createTeam(
            String teamName,
            String displayName,
            String prefix,
            String suffix,
            List<String> playerNames,
            Player player
    ) {
        PacketPlayOutScoreboardTeam packet = createTeamPacket(
                (byte) 0,
                teamName,
                prefix,
                displayName,
                suffix,
                playerNames
        );

        // teamName
        // mode
        // displayName
        // prefix
        // suffix
        // friendlyFlags (default 0)
        // nameTagVisibility
        // teamColor (default RESET)

        sendPacket(packet, player);
    }

    @Override
    public void updateTeam(
            String teamName,
            String displayName,
            String prefix,
            String suffix,
            List<String> playerNames,
            Player player
    ) {
        PacketPlayOutScoreboardTeam packet = createTeamPacket(
                (byte) 2,
                teamName,
                prefix,
                displayName,
                suffix,
                playerNames
        );

        // teamName
        // mode
        // displayName
        // prefix
        // suffix
        // friendlyFlags (default 0)
        // nameTagVisibility
        // teamColor (default RESET)

        sendPacket(packet, player);
    }

    @Override
    public void removeTeam(String teamName, Player player) {
        PacketPlayOutScoreboardTeam packet = removeTeamPacket(teamName);

        sendPacket(packet, player);
    }

    private PacketPlayOutScoreboardTeam removeTeamPacket(String teamName) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        PacketDataSerializer serializer = new PacketDataSerializer(null) {
            @Override
            public String c(int i) {
                return teamName;
            }

            @Override
            public byte readByte() {
                return 1;
            }
        };

        write(packet, serializer);
        return packet;
    }

    private PacketPlayOutScoreboardTeam createTeamPacket(
            byte mode,
            String teamName,
            String prefix,
            String displayName,
            String suffix,
            List<String> playerNames
    ) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        PacketDataSerializer serializer = new PacketDataSerializer(null) {

            final Iterator<String> iterator = playerNames.iterator();
            int fieldCount = 1;

            @Override
            public String c(int i) {
                int temp = fieldCount;
                fieldCount++;

                switch (temp) {
                    case 1: {
                        return teamName;
                    }
                    case 3: {
                        return displayName;
                    }
                    case 4: {
                        return prefix;
                    }
                    case 5: {
                        return suffix;
                    }
                    case 7: {
                        return "always";
                    }
                }

                if (i == 40) {
                    return iterator.next();
                }

                throw new RuntimeException();
            }

            @Override
            public byte readByte() {
                int temp = fieldCount;
                fieldCount++;

                switch (temp) {
                    case 2:
                        return mode;
                    case 6: {
                        return 0;
                    }
                    case 8: {
                        return -1;
                    }
                }

                throw new RuntimeException();
            }

            @Override
            public int e() {
                return playerNames.size();
            }
        };

        write(packet, serializer);
        return packet;
    }

    private void sendScorePacket(
            String entryName,
            String objectiveName,
            PacketPlayOutScoreboardScore.EnumScoreboardAction scoreboardAction,
            int score,
            Player player
    ) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();

        PacketDataSerializer serializer = newSerializer();
        serializer.a(entryName);
        serializer.a(scoreboardAction);
        serializer.a(objectiveName);
        serializer.b(score);

        write(packet, serializer);
        sendPacket(packet, player);
    }

    private void sendObjectivePacket(String name, String displayName, int action, Player player) {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();

        PacketDataSerializer serializer = newSerializer();
        serializer.a(name);
        serializer.writeByte(action);
        serializer.a(displayName);
        serializer.a("integer");

        write(packet, serializer);
        sendPacket(packet, player);
    }

    private PacketDataSerializer newSerializer() {
        return new PacketDataSerializer(Unpooled.buffer());
    }

    private void write(Packet<?> packet, PacketDataSerializer packetDataSerializer) {
        try {
            packet.a(packetDataSerializer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendPacket(Packet<?> packet, Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }
}
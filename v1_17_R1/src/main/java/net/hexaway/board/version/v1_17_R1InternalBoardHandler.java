package net.hexaway.board.version;

import net.hexaway.board.abstraction.InternalBoardHandler;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class v1_17_R1InternalBoardHandler implements InternalBoardHandler {

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
        PacketDataSerializer serializer = new PacketDataSerializer(null) {
            @Override
            public byte readByte() {
                return 1;
            }

            @Override
            public String e(int i) {
                return name;
            }
        };

        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective(serializer);

        sendPacket(packet, player);
    }

    @Override
    public void createOrUpdateScore(String entryName, String objectiveName, int score, Player player) {
        sendScorePacket(entryName, objectiveName, ScoreboardServer.Action.a, score, player);
    }

    @Override
    public void removeScore(String entryName, String objectiveName, Player player) {
        sendScorePacket(entryName, objectiveName, ScoreboardServer.Action.b, 0, player);
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
        PacketDataSerializer serializer = new PacketDataSerializer(null) {
            @Override
            public String e(int i) {
                return teamName;
            }

            @Override
            public byte readByte() {
                return 1;
            }
        };

        return new PacketPlayOutScoreboardTeam(serializer);
    }

    private PacketPlayOutScoreboardTeam createTeamPacket(
            byte mode,
            String teamName,
            String prefix,
            String displayName,
            String suffix,
            List<String> playerNames
    ) {
        PacketDataSerializer serializer = new PacketDataSerializer(null) {

            final Iterator<String> iterator = playerNames.iterator();
            int fieldCount = 1;

            @Override
            @SuppressWarnings("unchecked")
            public <T> List<T> a(Function<PacketDataSerializer, T> function) {
                return (List<T>) playerNames;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T extends Enum<T>> T a(Class<T> oclass) {
                fieldCount++;
                return (T) EnumChatFormat.v;
            }

            @Override
            public IChatBaseComponent i() {
                int temp = fieldCount++;
                switch (temp) {
                    case 3 -> {
                        return new ChatComponentText(displayName);
                    }
                    case 8 -> {
                        return new ChatComponentText(prefix);
                    }
                    case 9 -> {
                        return new ChatComponentText(suffix);
                    }
                }

                throw new RuntimeException();
            }

            @Override
            public String e(int i) {
                int temp = fieldCount++;

                if (i == 16)
                    return teamName;

                switch (temp) {
                    case 5:
                    case 6:
                        return "always";
                }

                if (i == 40) {
                    return iterator.next();
                }

                throw new RuntimeException();
            }

            @Override
            public byte readByte() {
                int temp = fieldCount++;

                if (temp == 2)
                    return mode;

                return 0;
            }
        };

        return new PacketPlayOutScoreboardTeam(serializer);
    }

    private void sendScorePacket(
            String entryName,
            String objectiveName,
            ScoreboardServer.Action scoreboardAction,
            int score,
            Player player
    ) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(scoreboardAction, objectiveName, entryName, score);

        sendPacket(packet, player);
    }

    private void sendObjectivePacket(String name, String displayName, int action, Player player) {
        PacketDataSerializer serializer = new PacketDataSerializer(null) {

            @Override
            public String e(int i) {
                return name;
            }

            @Override
            public byte readByte() {
                return (byte) action;
            }

            @Override
            public IChatBaseComponent i() {
                return new ChatComponentText(displayName);
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T extends Enum<T>> T a(Class<T> oclass) {
                return (T) IScoreboardCriteria.EnumScoreboardHealthDisplay.a;
            }
        };

        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(serializer);

        sendPacket(packet, player);
    }

    private void sendPacket(Packet<?> packet, Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.b.sendPacket(packet);
    }
}
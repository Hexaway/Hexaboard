import net.hexaway.board.CSLimitationProvider;
import net.hexaway.board.ServerLimitationProvider;
import net.hexaway.board.SimpleComplexBoardManager;
import net.hexaway.board.abstraction.*;
import net.hexaway.board.model.ScoreboardModelBuilder;
import net.hexaway.board.model.ScoreboardPack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        LimitationProvider limitationProvider;

        if (Bukkit.getPluginManager().isPluginEnabled("ClientStats")) { // Provide board limitations by player protocol
            limitationProvider = new CSLimitationProvider();
        } else {
            limitationProvider = new ServerLimitationProvider();
        }

        InternalBoardHandler boardHandler = InternalBoardHandlerProvider.get();

        ComplexBoardManager complexBoardManager = new SimpleComplexBoardManager(boardHandler, limitationProvider);

        ExampleListener exampleListener = new ExampleListener(createPack(), complexBoardManager, new PAPIInterceptor());

        Bukkit.getPluginManager().registerEvents(exampleListener, this);
    }

    private ScoreboardPack createPack() {
        String text = "mc.hexaway.net";
        List<String> serverIpFrames = new ArrayList<>();

        for (int i = 0; i < text.toCharArray().length; i++) {
            StringBuilder builder = new StringBuilder(text);

            builder.insert(i, ChatColor.GOLD);

            int nextChar = i + 3;

            if (nextChar < builder.length()) {
                builder.insert(nextChar, ChatColor.GREEN);
            }

            builder.insert(0, ChatColor.GREEN);
            serverIpFrames.add(builder.toString());
        }

        return new ScoreboardModelBuilder()
                .title(ScoreboardElement.singleFrame("&l&6Hexaway"))
                .line(5, "&eWelcome &b%player_name%&e!")
                .line(4, "Deaths: %statistic_deaths%")
                .line(3, "Kills: %statistic_player_kills%")
                .line(2, "Mined Blocks: %statistic_mine_block%")
                .line(1, "")
                .line(0, ScoreboardElement.withSameInterval(2, serverIpFrames))
                .build();
    }
}
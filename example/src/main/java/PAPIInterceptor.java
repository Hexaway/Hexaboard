import me.clip.placeholderapi.PlaceholderAPI;
import net.hexaway.board.abstraction.FrameInterceptor;
import org.bukkit.entity.Player;

public class PAPIInterceptor implements FrameInterceptor {

    @Override
    public String interceptLineFrame(String text, Player player) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    @Override
    public String interceptTitleFrame(String title, Player player) {
        return PlaceholderAPI.setPlaceholders(player, title);
    }
}
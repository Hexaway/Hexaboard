import net.hexaway.board.abstraction.FrameInterceptor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ColorInterceptor implements FrameInterceptor {

    private final FrameInterceptor delegate;

    public ColorInterceptor(FrameInterceptor delegate) {
        this.delegate = delegate;
    }

    @Override
    public String interceptLineFrame(String text, Player player) {
        return delegate.interceptLineFrame(colorize(text), player);
    }

    @Override
    public String interceptTitleFrame(String title, Player player) {
        return delegate.interceptTitleFrame(colorize(title), player);
    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
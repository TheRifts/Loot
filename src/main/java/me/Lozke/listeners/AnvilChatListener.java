package me.Lozke.listeners;

import me.Lozke.managers.AnvilManager;
import me.Lozke.utils.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AnvilChatListener implements Listener {

    private AnvilManager anvilManager;

    public AnvilChatListener(AnvilManager anvilManager) {
        this.anvilManager = anvilManager;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!anvilManager.isTracked(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
        switch (event.getMessage()) {
            case "y":
            case "Y":
                anvilManager.repairItem(event.getPlayer());
                break;
            case "n":
            case "N":
                event.getPlayer().sendMessage(Text.colorize("&cItem Repair - &lCANCELLED"));
                anvilManager.stopTracking(event.getPlayer());
                break;
            default:
                event.getPlayer().sendMessage(Text.colorize("&cInvalid Option &7- ") + AnvilManager.INPUT_INSTRUCTIONS);
                break;
        }
    }
}

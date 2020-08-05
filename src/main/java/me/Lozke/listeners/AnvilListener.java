package me.Lozke.listeners;

import me.Lozke.managers.AnvilManager;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.menus.RepairSelector;
import me.Lozke.utils.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AnvilListener implements Listener {

    @EventHandler
    public void onAnvilClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !(event.getClickedBlock().getBlockData().getAsString().contains("anvil"))) return;
        event.setCancelled(true);
        if (AnvilManager.getInstance().isTracked(event.getPlayer())) {
            event.getPlayer().sendMessage(Text.colorize("&cYou're already repairing an item"));
            return;
        }
        ItemWrapper itemWrapper = new ItemWrapper(event.getPlayer().getInventory().getItemInMainHand());
        if (!itemWrapper.isRealItem()) return;
        if (itemWrapper.getDurabilityAsPercentage() > 0.99) {
            event.getPlayer().sendMessage(Text.colorize("&cThis item is already repaired."));
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) handleRightClick(event);
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) handleLeftClick(event);
    }

    private void handleRightClick(PlayerInteractEvent event) {
        if (AnvilManager.getInstance().isTracked(event.getPlayer())) {
            return;
        }
        AnvilManager.getInstance().handleEvent(event);
    }

    private void handleLeftClick(PlayerInteractEvent event) {
        new RepairSelector(event.getItem()).openMenu(event.getPlayer());
    }

}

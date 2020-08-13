package me.Lozke.listeners;

import me.Lozke.managers.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ScrollRightClickListener implements Listener {

    @EventHandler
    public void onMapInitialize(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getType() != Material.MAP) return;
        if (new ItemWrapper(event.getItem()).isRealItem()) {
            event.setCancelled(true);
        }
    }

}

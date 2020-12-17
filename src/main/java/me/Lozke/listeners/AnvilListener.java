package me.Lozke.listeners;

import me.Lozke.managers.AnvilManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class AnvilListener implements Listener {

    private AnvilManager anvilManager;

    public AnvilListener(AnvilManager anvilManager) {
        this.anvilManager = anvilManager;
    }

    @EventHandler
    public void onAnvilClick(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND || event.getClickedBlock() == null || !(event.getClickedBlock().getBlockData().getAsString().contains("anvil")))
            return;
        event.setCancelled(true);
        anvilManager.handleEvent(event);
    }
}

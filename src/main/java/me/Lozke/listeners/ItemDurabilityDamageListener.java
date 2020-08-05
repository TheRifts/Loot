package me.Lozke.listeners;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.managers.ItemWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemDurabilityDamageListener implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    private void onItemUsage(PlayerItemDamageEvent event) {
        ItemWrapper wrapper = new ItemWrapper(event.getItem());

        if (!wrapper.hasKey(ARNamespacedKey.REAL_ITEM) || event.isCancelled()) return;
        else event.setDamage(0);

        wrapper.addDurability(-1);
    }
}

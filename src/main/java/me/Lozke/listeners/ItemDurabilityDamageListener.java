package me.Lozke.listeners;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.managers.ItemWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemDurabilityDamageListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onItemUsage(PlayerItemDamageEvent event) {
        ItemWrapper wrapper = new ItemWrapper(event.getItem());
        if (!wrapper.hasKey(ARNamespacedKey.REAL_ITEM)) return;
        else event.setDamage(0);
        wrapper.addDurability(-1);
        if (wrapper.getDurabality() <= 0) {
            event.getItem().setAmount(0);
        }
    }
}

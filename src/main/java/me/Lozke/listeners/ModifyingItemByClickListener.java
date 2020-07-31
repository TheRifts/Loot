package me.Lozke.listeners;

import me.Lozke.managers.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ModifyingItemByClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || !inventory.getType().equals(InventoryType.PLAYER) || event.getAction() != InventoryAction.SWAP_WITH_CURSOR || event.getCursor() == null || event.getCurrentItem() == null) {
            return;
        }

        ItemWrapper cursorItem = new ItemWrapper(event.getCursor());
        ItemWrapper currentItem = new ItemWrapper(event.getCurrentItem());

        if (!cursorItem.isRealItem() || !currentItem.isTiered() || !cursorItem.isTiered() || cursorItem.getTier() != currentItem.getTier()) {
            return;
        }

        //Orb
        if (cursorItem.getItem().getType().equals(Material.MAGMA_CREAM)) {
            event.setCancelled(true);
            currentItem.randomizeAttributes().format();
        }
        //Shard
        else if (cursorItem.getItem().getType().equals(Material.BLAZE_POWDER)) {
            event.setCancelled(true);
            currentItem.randomizeStats().format();
        }
        else {
            return;
        }

        //Consume the item
        cursorItem.getItem().setAmount(cursorItem.getItem().getAmount() - 1);
    }
}

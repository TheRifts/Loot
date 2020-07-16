package me.Lozke.listeners;

import me.Lozke.data.Tier;
import me.Lozke.managers.ItemHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ModifyingItemByClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack cursorItem = event.getCursor();
        ItemStack currentItem = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();

        if (inventory == null ||
                !event.getClickedInventory().getType().equals(InventoryType.PLAYER) ||
                !ItemHandler.isTiered(currentItem) ||
                !ItemHandler.isTiered(cursorItem)) {
            return;
        }

        Tier tierCursor = ItemHandler.getTier(cursorItem);
        Tier tierCurrent = ItemHandler.getTier(currentItem);
        //Ensure item is same tier as item it is being applied to
        if (tierCursor!= tierCurrent) {
            return;
        }

        //Orb
        if (cursorItem.getType().equals(Material.MAGMA_CREAM)) {
            event.setCancelled(true);
            ItemHandler.randomizeAttributes(currentItem);
        }
        //Shard
        else if (cursorItem.getType().equals(Material.BLAZE_POWDER)) {
            event.setCancelled(true);
            ItemHandler.randomizeStats(currentItem);
        }
        else {
            return;
        }

        //Consume the item
        cursorItem.setAmount(cursorItem.getAmount() - 1);
    }
}

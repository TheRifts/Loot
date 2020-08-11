package me.Lozke.listeners;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.Tier;
import me.Lozke.managers.ItemWrapper;
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

        switch (cursorItem.getItem().getType()) {
            case MAGMA_CREAM: //Orb
                event.setCancelled(true);
                if (!currentItem.getBoolean(ARNamespacedKey.CAN_ORB)) return;
                currentItem.randomizeAttributes().format();
                break;
            case BLAZE_POWDER: //Shard
                event.setCancelled(true);
                currentItem.randomizeStats().format();
                break;
            case LEATHER:
                if (currentItem.getTier() == Tier.T1) {
                    event.setCancelled(true);
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case IRON_BARS:
                if (currentItem.getTier() == Tier.T2) {
                    event.setCancelled(true);
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case LIGHT_GRAY_DYE:
                if (currentItem.getTier() == Tier.T3) {
                    event.setCancelled(true);
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case LIGHT_BLUE_DYE:
                if (currentItem.getTier() == Tier.T4) {
                    event.setCancelled(true);
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case YELLOW_DYE:
                if (currentItem.getTier() == Tier.T5) {
                    event.setCancelled(true);
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case BLACK_DYE:
                if (currentItem.getTier() == Tier.T6) {
                    event.setCancelled(true);
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            default:
                return;
        }

        //Consume the item
        cursorItem.getItem().setAmount(cursorItem.getItem().getAmount() - 1);
    }
}

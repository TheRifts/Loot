package me.Lozke.listeners;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.items.Scroll.ScrollData;
import me.Lozke.data.Tier;
import me.Lozke.items.Scroll.ScrollDataTag;
import me.Lozke.utils.ItemWrapper;
import me.Lozke.utils.NumGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ModifyingItemByClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || !inventory.getType().equals(InventoryType.PLAYER) || event.getAction() != InventoryAction.SWAP_WITH_CURSOR || event.getCursor() == null || event.getCurrentItem() == null) {
            return;
        }

        ItemWrapper cursorItem = new ItemWrapper(event.getCursor());
        ItemWrapper currentItem = new ItemWrapper(event.getCurrentItem());

        if (!cursorItem.isRealItem() || !currentItem.isRealItem()) {
            return;
        }

        //Prevent item from being swapped
        event.setCancelled(true);

        switch (cursorItem.getItem().getType()) {
            case MAGMA_CREAM: //Orb
                if (!currentItem.getBoolean(ARNamespacedKey.CAN_ORB)) return;
                currentItem.randomizeStats().format();
                break;
            case BLAZE_POWDER: //Shard
                currentItem.randomizeStatValues().format();
                break;
            case LEATHER:
                if (currentItem.getTier() == Tier.T1) {
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case IRON_BARS:
                if (currentItem.getTier() == Tier.T2) {
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case LIGHT_GRAY_DYE:
                if (currentItem.getTier() == Tier.T3) {
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case LIGHT_BLUE_DYE:
                if (currentItem.getTier() == Tier.T4) {
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case YELLOW_DYE:
                if (currentItem.getTier() == Tier.T5) {
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case BLACK_DYE:
                if (currentItem.getTier() == Tier.T6) {
                    currentItem.setDurabilityAsPercentage(currentItem.getDurabilityAsPercentage() + 0.03);
                }
                break;
            case MAP: //Scroll
                List<ScrollData> usedScrolls = currentItem.getList(ARNamespacedKey.USED_SCROLLS);
                if (usedScrolls.size() >= currentItem.getInt(ARNamespacedKey.SCROLL_MAX_AMOUNT)) {
                    event.setCancelled(false);
                    return;
                }
                ScrollData scrollData = (ScrollData) cursorItem.get(ScrollDataTag.DATA_TAG, new ScrollDataTag());
                double roll = NumGenerator.fraction();
                if (roll < scrollData.getSuccessPercent()) {
                    usedScrolls.add(scrollData);
                }
                else {
                    roll = NumGenerator.fraction();
                    if (roll < scrollData.getDestroyPercent()) { //Destroy
                        inventory.remove(currentItem.getItem());
                        break;
                    }
                    else { //Add null to used scrolls list if item does not get destroyed
                        usedScrolls.add(null);
                    }
                }
                currentItem.addKey(ARNamespacedKey.USED_SCROLLS, usedScrolls);
                currentItem.format();
                break;
            default:
                event.setCancelled(false);
                return;
        }

        //Consume the item
        cursorItem.getItem().setAmount(cursorItem.getItem().getAmount() - 1);
    }
}

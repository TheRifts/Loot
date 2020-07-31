package me.Lozke.listeners;

import me.Lozke.data.Gem;
import me.Lozke.data.PouchInventory;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.managers.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PouchListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof PouchInventory) {
            HashMap<Integer, byte[]> map = new HashMap<>();
            int i = 0;

            for (ItemStack item : event.getInventory().getContents()) {
                map.put(i, item == null ? null : item.serializeAsBytes());
                i++;
            }

            int rawSlot = ((PouchInventory) event.getInventory().getHolder()).getRawSlot();
            ItemStack item = event.getPlayer().getOpenInventory().getItem(rawSlot);
            if (item != null) {
                new ItemWrapper(item).addKey(ARNamespacedKey.HELD_ITEMS, map);
            }

            ((Player) event.getPlayer()).updateInventory();
        }
    }

    @EventHandler
    public void onInventoryInteraction(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || event.getCurrentItem() == null) {
            return;
        }

        ItemWrapper clickedItemWrapper = new ItemWrapper(event.getCurrentItem());

        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR && event.getCursor() != null) {
            handleDeposit(event, clickedItemWrapper, new ItemWrapper(event.getCursor()));
        }

        if (event.getClick() == ClickType.DOUBLE_CLICK || event.getClick() == ClickType.SHIFT_RIGHT) {
            //Handle Double Click
            if (event.getClick() == ClickType.DOUBLE_CLICK && event.getCursor() != null && event.getCursor().getType() == Material.INK_SAC) {
                ItemStack currentItem = event.getCurrentItem();
                ItemStack cursorItem = event.getCursor();

                //Swap Cursor and Clicked Item
                event.getClickedInventory().setItem(event.getSlot(), cursorItem);
                event.getWhoClicked().setItemOnCursor(currentItem);
                ((Player) event.getWhoClicked()).updateInventory();

                //Reinitializing item wrapper to wrap pouch instead of former cursor
                clickedItemWrapper = new ItemWrapper(cursorItem);
            }
            handleInventory(event, clickedItemWrapper);
            event.setCancelled(true);
        }

        if (event.getClick() == ClickType.RIGHT && clickedItemWrapper.getItem().getAmount() > 0 && clickedItemWrapper.getInt(ARNamespacedKey.GEM_WORTH) != 0) {
            handleWithdraw(event, clickedItemWrapper);
        }
    }

    private void handleDeposit(InventoryClickEvent event, ItemWrapper clickedItem, ItemWrapper cursorItem) {
        if (clickedItem.getItem().getType() != Material.INK_SAC || !clickedItem.isRealItem()) {
            return;
        }

        if (cursorItem.isRealItem() && cursorItem.getInt(ARNamespacedKey.GEM_WORTH) > 0) {
            event.setCancelled(true);

            int maxItemValue = clickedItem.getInt(ARNamespacedKey.MAX_GEM_WORTH);
            int currentItemValue = clickedItem.getInt(ARNamespacedKey.GEM_WORTH);
            int cursorValue = cursorItem.getInt(ARNamespacedKey.GEM_WORTH) * cursorItem.getItem().getAmount();

            int depositAmount = cursorValue - Math.max(0, (currentItemValue + cursorValue) - maxItemValue);

            if (depositAmount < 1) {
                return;
            }

            clickedItem.addKey(ARNamespacedKey.GEM_WORTH, (currentItemValue + depositAmount));

            if (cursorItem.getItem().getType() == Material.EMERALD) {
                event.getWhoClicked().getItemOnCursor().setAmount(Math.max(0, cursorItem.getItem().getAmount() - depositAmount));
                ((Player) event.getWhoClicked()).updateInventory();
                return;
            }

            int newValue = Math.max(0, cursorValue - depositAmount);
            if (newValue == 0) {
                event.getWhoClicked().getItemOnCursor().setAmount(0);
                ((Player) event.getWhoClicked()).updateInventory();
            }
            else {
                cursorItem.addKey(ARNamespacedKey.GEM_WORTH, Math.max(0, newValue));
            }
        }
    }

    private void handleWithdraw(InventoryClickEvent event, ItemWrapper clickedItem) {
        if (clickedItem.getItem().getType() != Material.INK_SAC || !clickedItem.isRealItem()) {
            return;
        }

        event.setCancelled(true);

        int balance = clickedItem.getInt(ARNamespacedKey.GEM_WORTH);
        int withdrawAmount = Math.min(64, balance);
        clickedItem.addKey(ARNamespacedKey.GEM_WORTH, balance - withdrawAmount);

        event.getWhoClicked().setItemOnCursor(Gem.getItem(withdrawAmount));
        ((Player) event.getWhoClicked()).updateInventory();
    }

    private void handleInventory(InventoryClickEvent event, ItemWrapper clickedItem) {
        if (clickedItem.getItem().getType() != Material.INK_SAC || !clickedItem.isRealItem()) {
            return;
        }

        PouchInventory pouchInventory = new PouchInventory(event.getRawSlot());
        Inventory inventory = pouchInventory.getInventory();

        Map heldItems = clickedItem.getMap(ARNamespacedKey.HELD_ITEMS);

        if (heldItems.isEmpty()) {
            return;
        }

        for (Object key : heldItems.keySet()) {
            if (heldItems.get(key) != null && ((byte[]) heldItems.get(key)).length > 0) {
                ItemStack item = ItemStack.deserializeBytes((byte[]) heldItems.get(key));
                inventory.setItem((int) key, item);
            }
        }

        event.getWhoClicked().openInventory(inventory);
    }
}

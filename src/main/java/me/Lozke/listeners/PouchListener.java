package me.Lozke.listeners;

import me.Lozke.items.Gem;
import me.Lozke.data.PouchInventory;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.utils.ItemWrapper;
import me.Lozke.utils.NamespacedKeyWrapper;
import me.Lozke.utils.Text;
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
            int slot = 0;
            int nullCount = 0;
            for (ItemStack item : event.getInventory().getContents()) {
                if (item == null) {
                    nullCount++;
                }
                map.put(slot, item == null ? null : item.serializeAsBytes());
                slot++;
            }
            if (nullCount == event.getInventory().getSize()) {
                map = new HashMap<>();
            }

            int rawSlot = ((PouchInventory) event.getInventory().getHolder()).getRawSlot();
            ItemStack item = event.getPlayer().getOpenInventory().getItem(rawSlot);
            if (item != null) {
                NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(item);
                wrapper.addKey(ARNamespacedKey.HELD_ITEMS, map);
                if (map.isEmpty()) {
                    wrapper.removeKey(ARNamespacedKey.UUID);
                }
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
        if ((event.getSlotType() != InventoryType.SlotType.CONTAINER && event.getSlotType() != InventoryType.SlotType.QUICKBAR) || (event.getSlotType() == InventoryType.SlotType.QUICKBAR && event.getRawSlot() == 45)) {
            return;
        }

        ItemWrapper clickedItemWrapper = new ItemWrapper(event.getCurrentItem());

        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR && event.getCursor() != null) {
            handleDeposit(event, clickedItemWrapper, new ItemWrapper(event.getCursor()));
        }

        if (event.getClick() == ClickType.DOUBLE_CLICK || event.getClick() == ClickType.SHIFT_RIGHT) {
            //Handle Double Click
            if (event.getClick() == ClickType.DOUBLE_CLICK && event.getCursor() != null && event.getCursor().getType() == Material.INK_SAC && event.getCursor().getAmount() == 1) {
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

    @EventHandler
    public void onPouchInventoryInteraction(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof PouchInventory) {
            //Prevent putting pouches inside PouchInventory
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.INK_SAC) {
                if (event.isShiftClick()) {
                    event.setCancelled(true);
                }
            }
            if (event.getCursor() != null && event.getCursor().getType() == Material.INK_SAC) {
                if (event.getRawSlot() < event.getInventory().getSize()) {
                    event.setCancelled(true);
                }
            }

            int rawSlot = ((PouchInventory) event.getInventory().getHolder()).getRawSlot();
            //Fixes clicking opened pouch inventory
            if (event.getRawSlot() == rawSlot) {
                event.setCancelled(true);
                return;
            }
            //Fixes swapping pouch slot when it's on the hot bar
            if (event.getClick() == ClickType.NUMBER_KEY) {
                //36 == slot 1 of hot bar
                if (36 + event.getHotbarButton() == rawSlot) {
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onPouchInventoryInteraction(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof PouchInventory) {
            boolean containsPouch = false;
            for (ItemStack is : event.getNewItems().values()) {
                if (is.getType() == Material.INK_SAC) {
                    containsPouch = true;
                }
            }
            if (containsPouch) {
                for (Integer rawSlot : event.getRawSlots()) {
                    if (rawSlot < event.getInventory().getSize()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private void handleDeposit(InventoryClickEvent event, ItemWrapper clickedItem, ItemWrapper cursorItem) {
        if (clickedItem.getItem().getType() != Material.INK_SAC || !clickedItem.isRealItem()) {
            return;
        }
        if (isStacked((Player) event.getWhoClicked(), clickedItem.getItem())) return;

        if (cursorItem.isRealItem() && cursorItem.getInt(ARNamespacedKey.GEM_WORTH) > 0) {
            event.setCancelled(true);

            int maxItemValue = clickedItem.getInt(ARNamespacedKey.MAX_GEM_WORTH);
            int currentItemValue = clickedItem.getInt(ARNamespacedKey.GEM_WORTH);
            int cursorValue = cursorItem.getInt(ARNamespacedKey.GEM_WORTH) * cursorItem.getItem().getAmount();
            int depositAmount = cursorValue - Math.max(0, (currentItemValue + cursorValue) - maxItemValue);
            int newCursorValue = Math.max(0, cursorValue - depositAmount);

            if (depositAmount < 1) {
                return;
            }

            clickedItem.addKey(ARNamespacedKey.GEM_WORTH, (currentItemValue + depositAmount));

            if (cursorItem.getItem().getType() == Material.EMERALD) {
                event.getWhoClicked().getItemOnCursor().setAmount(Math.max(0, cursorItem.getItem().getAmount() - depositAmount));
                ((Player) event.getWhoClicked()).updateInventory();
                return;
            }
            else if (cursorItem.getItem().getType() == Material.PAPER) {
                if (newCursorValue == 0) {
                    cursorItem.getItem().setAmount(0);
                    ((Player) event.getWhoClicked()).updateInventory();
                    return;
                }
                cursorItem.setLore(Text.colorize("&fValue: &a" + newCursorValue));
            }
            cursorItem.addKey(ARNamespacedKey.GEM_WORTH, newCursorValue);
        }
    }

    private void handleWithdraw(InventoryClickEvent event, ItemWrapper clickedItem) {
        if (clickedItem.getItem().getType() != Material.INK_SAC || !clickedItem.isRealItem()) {
            return;
        }
        if (isStacked((Player) event.getWhoClicked(), clickedItem.getItem())) return;

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
        if (isStacked((Player) event.getWhoClicked(), clickedItem.getItem())) return;

        PouchInventory pouchInventory = new PouchInventory(event.getRawSlot());
        Inventory inventory = pouchInventory.getInventory();

        Map heldItems = clickedItem.getMap(ARNamespacedKey.HELD_ITEMS);
        if (heldItems != null) {
            for (Object key : heldItems.keySet()) {
                if (heldItems.get(key) != null && ((byte[]) heldItems.get(key)).length > 0) {
                    ItemStack item = ItemStack.deserializeBytes((byte[]) heldItems.get(key));
                    inventory.setItem((int) key, item);
                }
            }
        }

        clickedItem.addKey(ARNamespacedKey.UUID);
        event.getWhoClicked().getInventory().setItem(event.getSlot(), clickedItem.getItem());

        event.getWhoClicked().openInventory(inventory);
    }

    private boolean isStacked(Player player, ItemStack stack) {
        if (stack.getAmount() > 1) {
            player.sendMessage(Text.colorize("&cUnstack your pouches to use them!"));
            return true;
        }
        return false;
    }
}

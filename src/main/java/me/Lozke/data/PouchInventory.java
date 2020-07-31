package me.Lozke.data;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PouchInventory implements InventoryHolder {

    private int rawSlot;
    private Inventory inventory;

    public PouchInventory(int rawSlot, int size) {
        this.rawSlot = rawSlot;
        this.inventory = Bukkit.createInventory(this, size * 9, "Pouch Inventory");
    }

    public PouchInventory(int rawSlot) {
        this(rawSlot, 1);
    }

    public int getRawSlot() {
        return rawSlot;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

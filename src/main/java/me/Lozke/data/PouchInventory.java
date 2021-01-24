package me.Lozke.data;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Data
public class PouchInventory implements InventoryHolder {

    private final int rawSlot;
    private final Inventory inventory;

    public PouchInventory(int rawSlot, int size) {
        this.rawSlot = rawSlot;
        this.inventory = Bukkit.createInventory(this, size * 9, "Pouch Inventory");
    }
    public PouchInventory(int rawSlot) {
        this(rawSlot, 1);
    }
}

package me.Lozke.data;

import lombok.Data;
import me.Lozke.managers.AnvilManager;
import me.Lozke.utils.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

@Data
public class RepairContainer {

    private final Map<EquipmentSlot, ItemStack> slotItemMap = new HashMap<>();
    private Item item;

    public RepairContainer(PlayerInventory inventory, AnvilManager.RepairType type) {
        switch (type) {
            case HAND:
                cacheSlot(inventory, EquipmentSlot.HAND);
                break;
            case SET:
                EquipmentSlot[] slots = EquipmentSlot.values();
                for (EquipmentSlot slot : slots) {
                    cacheSlot(inventory, slot);
                }
                break;
        }
    }

    private void cacheSlot(PlayerInventory inventory, EquipmentSlot slot) {
        if (slot == EquipmentSlot.OFF_HAND) return;
        if (inventory.getItem(slot).getType() == Material.AIR) return;

        ItemWrapper wrapper = new ItemWrapper(inventory.getItem(slot));
        if (!wrapper.isRealItem()) return;
        if (wrapper.getDurabality() == wrapper.getMaxDurability()) return;

        slotItemMap.put(slot, inventory.getItem(slot));
        inventory.setItem(slot, null);
    }
}

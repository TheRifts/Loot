package me.Lozke.managers;

import me.Lozke.data.EquipmentContainer;
import me.Lozke.data.RiftsStat;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class EquipmentManager {

    public  final static EquipmentSlot[] slots = EquipmentSlot.values();

    public void updateContainer(EquipmentContainer container, Inventory inventory) {
        if (!(inventory instanceof PlayerInventory)) {
            return;
        }
        for (EquipmentSlot slot : slots) {
            ItemStack newEquipment = ((PlayerInventory) inventory).getItem(slot);
            updateSlot(container, slot, newEquipment);
        }
    }

    public void updateSlot(EquipmentContainer container, EquipmentSlot slot, ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) {
            container.clearSlot(slot);
            return;
        }
        if (isSameHash(container.getSlotHashCode(slot), stack.hashCode())) {
            return;
        }
        ItemWrapper wrapper = new ItemWrapper(stack);
        if (!wrapper.isRealItem()) {
            container.clearSlot(slot);
            return;
        }
        Map<RiftsStat, Integer> itemStats = StatManager.combineStatMaps(wrapper.getMinorStats(), wrapper.getMajorStats());
        container.setSlotHashCode(slot, stack.hashCode());
        container.setSlotStats(slot, itemStats);
    }

    private boolean isSameHash(int hash1, int hash2) {
        return hash1 == hash2;
    }
}

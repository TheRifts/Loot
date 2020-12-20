package me.Lozke.managers;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.EquipmentContainer;
import me.Lozke.data.RiftsStat;
import me.Lozke.data.WeaponType;
import me.Lozke.utils.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;
import java.util.UUID;

public class EquipmentManager {

    public final static EquipmentSlot[] slots = EquipmentSlot.values();

    public boolean updateContainer(EquipmentContainer container, Inventory inventory) {
        if (!(inventory instanceof PlayerInventory)) {
            return false;
        }
        boolean updated = false;
        for (EquipmentSlot slot : slots) {
            if (slot == EquipmentSlot.OFF_HAND) continue;
            ItemStack newEquipment = ((PlayerInventory) inventory).getItem(slot);
            if (updateSlot(container, slot, newEquipment) && !updated) updated = true;
        }
        return updated;
    }

    public boolean updateSlot(EquipmentContainer container, EquipmentSlot slot, ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) {
            if (container.getSlotUUID(slot) == null) return false; //Slot is already cleared
            container.clearSlot(slot);
            return true;
        }
        ItemWrapper wrapper = new ItemWrapper(stack);
        if (!wrapper.isRealItem()) {
            if (container.getSlotUUID(slot) == null) return false; //Slot is already cleared
            container.clearSlot(slot);
            return true;
        }
        if (isSameUUID(container.getSlotUUID(slot), (UUID) wrapper.get(ARNamespacedKey.UUID))) {
            return false;
        }
        if (slot == EquipmentSlot.HAND) { //Guarantee the item in hand is actually a weapon
            if (WeaponType.getWeaponType(stack) == null) {
                container.clearSlot(slot);
                return true;
            }
        }
        Map<RiftsStat, Integer> itemStats = StatManager.combineStatMaps(wrapper.getMinorStats(), wrapper.getMajorStats());
        container.setSlotUUID(slot, (UUID) wrapper.get(ARNamespacedKey.UUID));
        container.setSlotStats(slot, itemStats);
        return true;
    }

    private boolean isSameUUID(UUID uuid1, UUID uuid2) {
        if (uuid1 == null || uuid2 == null) return false;
        return uuid1.equals(uuid2);
    }
}

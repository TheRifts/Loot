package me.Lozke.data;

import me.Lozke.LootPlugin;
import me.Lozke.managers.EquipmentManager;
import me.Lozke.managers.StatManager;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class EquipmentContainer {

    private final Map<EquipmentSlot, Integer> slotHashCodeMap = new HashMap<>();
    private final Map<EquipmentSlot, Map<RiftsStat, Integer>> slotStatsMap = new HashMap<>();

    private final Map<RiftsStat, Integer> combinedStats = new HashMap<>();

    public EquipmentContainer() {
        for (EquipmentSlot slot : EquipmentManager.slots) {
            slotStatsMap.put(slot, new HashMap<>(0));
        }
    }

    public EquipmentContainer(PlayerInventory inventory) {
        LootPlugin.getPluginInstance().getEquipmentManager().updateContainer(this, inventory);
        combineStats();
    }

    public void clearSlot(EquipmentSlot slot) {
        this.slotHashCodeMap.remove(slot);
        this.slotStatsMap.get(slot).clear();
    }

    public int getSlotHashCode(EquipmentSlot slot) {
        return slotHashCodeMap.getOrDefault(slot, 0);
    }

    public void setSlotHashCode(EquipmentSlot slot, int hash) {
        this.slotHashCodeMap.put(slot, hash);
    }

    public Map<RiftsStat, Integer> getSlotStats(EquipmentSlot slot) {
        return slotStatsMap.get(slot);
    }

    public void setSlotStats(EquipmentSlot slot, Map<RiftsStat, Integer> stats) {
        this.slotStatsMap.get(slot).clear();
        this.slotStatsMap.put(slot, stats);
    }

    public Map<RiftsStat, Integer> getCombinedStats() {
        return combinedStats;
    }

    public void combineStats() {
        combinedStats.clear();
        combinedStats.putAll(StatManager.combineStatMaps(
                slotStatsMap.get(EquipmentSlot.HEAD),
                slotStatsMap.get(EquipmentSlot.CHEST),
                slotStatsMap.get(EquipmentSlot.LEGS),
                slotStatsMap.get(EquipmentSlot.FEET),
                slotStatsMap.get(EquipmentSlot.HAND)
        ));
    }
}

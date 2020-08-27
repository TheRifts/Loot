package me.Lozke.data;

import me.Lozke.utils.Logger;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ArmourType {
    HELMET("HELMET", EquipmentSlot.HEAD, true),
    CHESTPLATE("CHESTPLATE", EquipmentSlot.CHEST, true),
    LEGGINGS("LEGGINGS", EquipmentSlot.LEGS,true),
    BOOTS("BOOTS", EquipmentSlot.FEET, true);

    private String itemType;
    private EquipmentSlot equipmentSlot;
    private boolean tiered;

    ArmourType(String itemType, EquipmentSlot equipmentSlot, boolean tiered) {
        this.itemType = itemType;
        this.equipmentSlot = equipmentSlot;
        this.tiered = tiered;
    }

    public static ArmourType[] types = ArmourType.values();

    public String getItemType() {
        return itemType;
    }
    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public ItemStack getItem() {
        return getItem(Tier.T1);
    }
    public ItemStack getItem(Tier tier) {
        Material material;
        if (tiered) material = Material.getMaterial(tier.getArmorMaterial() + "_" + itemType);
        else material = Material.getMaterial(itemType);
        if (material == null) {
            material = Material.AIR;
            Logger.error("Invalid weapon material. Item Type: " + itemType + " Tier: " + tier);
        }
        return new ItemStack(material);
    }
}

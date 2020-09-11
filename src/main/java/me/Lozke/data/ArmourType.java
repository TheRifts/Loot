package me.Lozke.data;

import me.Lozke.utils.Logger;
import me.Lozke.utils.NumGenerator;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum ArmourType {
    HELMET("HELMET", EquipmentSlot.HEAD),
    CHESTPLATE("CHESTPLATE", EquipmentSlot.CHEST),
    LEGGINGS("LEGGINGS", EquipmentSlot.LEGS),
    BOOTS("BOOTS", EquipmentSlot.FEET);

    private String itemType;
    private EquipmentSlot equipmentSlot;

    ArmourType(String itemType, EquipmentSlot equipmentSlot) {
        this.itemType = itemType;
        this.equipmentSlot = equipmentSlot;
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
        Material material = Material.getMaterial(tier.getArmorMaterial() + "_" + itemType);
        if (material == null) {
            material = Material.AIR;
            Logger.error("Invalid weapon material. Item Type: " + itemType + " Tier: " + tier);
        }
        return new ItemStack(material);
    }

    public static ArmourType fromEquipmentSlot(EquipmentSlot slot) {
        switch (slot) {
            case HEAD:
                return ArmourType.HELMET;
            case CHEST:
                return ArmourType.CHESTPLATE;
            case LEGS:
                return ArmourType.LEGGINGS;
            case FEET:
                return ArmourType.BOOTS;
            default:
                return null;
        }
    }
    public static ArmourType getRandomArmourType() {
        return types[NumGenerator.index(types.length)];
    }
}

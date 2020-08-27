package me.Lozke.data;

import me.Lozke.utils.Logger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArmourType {
    HELMET("HELMET", true),
    CHESTPLATE("CHESTPLATE", true),
    LEGGINGS("LEGGINGS", true),
    BOOTS("BOOTS", true);

    private String itemType;
    private boolean tiered;

    ArmourType(String itemType, boolean tiered) {
        this.itemType = itemType;
        this.tiered = tiered;
    }

    public static ArmourType[] types = ArmourType.values();

    public String getItemType() {
        return itemType;
    }

    public ItemStack getItem() {
        return getItem(Tier.T1);
    }
    public ItemStack getItem(Tier tier) {
        Material material;
        if (tiered) material = Material.getMaterial(tier.getWeaponMaterial() + "_" + itemType);
        else material = Material.getMaterial(itemType);
        if (material == null) {
            material = Material.AIR;
            Logger.error("Invalid weapon material. Item Type: " + itemType + " Tier: " + tier);
        }
        return new ItemStack(material);
    }
}

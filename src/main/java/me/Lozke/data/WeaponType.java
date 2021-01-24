package me.Lozke.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Lozke.utils.Logger;
import me.Lozke.utils.NumGenerator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum WeaponType {
    SWORD("SWORD", true),
    AXE("AXE", true),
    POLEARM("SHOVEL", true);
    //TEMPORARILY REMOVING TILL PACKET MOB DISGUISES ADDED
    //STAVE("HOE", true),
    //BOW("BOW", false),
    //CROSSBOW("CROSSBOW", false),
    //TRIDENT("TRIDENT", false);

    @Getter private final String itemType;
    @Getter private final boolean tiered;

    public static WeaponType[] types = WeaponType.values();

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

    public static ItemStack getRandomItem() {
        return getRandomItem(Tier.T1);
    }
    public static ItemStack getRandomItem(Tier tier) {
        List<WeaponType> weaponsList = Arrays.asList(types);
        return weaponsList.get(NumGenerator.index(weaponsList.size())).getItem(tier);
    }

    public static WeaponType getWeaponType(ItemStack itemStack) {
        String materialName = itemStack.getType().toString();
        for (WeaponType weaponType : types) {
            if (materialName.endsWith(weaponType.getItemType())) {
                return weaponType;
            }
        }
        return null;
    }
    public static WeaponType getRandomWeaponType() {
        return types[NumGenerator.index(types.length)];
    }
}

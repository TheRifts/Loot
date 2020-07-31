package me.Lozke.data;

import me.Lozke.managers.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Scrap {
    T1(Material.LEATHER),
    T2(Material.IRON_BARS),
    T3(Material.LIGHT_GRAY_DYE),
    T4(Material.LIGHT_BLUE_DYE),
    T5(Material.YELLOW_DYE),
    T6(Material.BLACK_DYE);

    public static Scrap[] types = Scrap.values();

    private final Material material;

    Scrap(Material material) {
        this.material = material;
    }

    public ItemStack getItem(int amount) {
        Tier tier = Tier.valueOf(this.name());
        return new ItemWrapper()
                .setMaterial(material)
                .setAmount(amount)
                .setTier(tier)
                .addKey(ARNamespacedKey.REAL_ITEM)
                .getItem();
    }

    public ItemStack getItem() {
        return getItem(1);
    }
}

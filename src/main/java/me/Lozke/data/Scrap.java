package me.Lozke.data;

import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Scrap {
    T1(Material.LEATHER, "Wooden/Leather"),
    T2(Material.IRON_BARS, "Stone/Chain"),
    T3(Material.LIGHT_GRAY_DYE, "Steel"),
    T4(Material.LIGHT_BLUE_DYE, "Ivory"),
    T5(Material.YELLOW_DYE, "Heavenly"),
    T6(Material.BLACK_DYE, "Demonic");

    public static Scrap[] types = Scrap.values();

    private final Material material;
    private final String materialString;

    Scrap(Material material, String materialString) {
        this.material = material;
        this.materialString = materialString;
    }

    public ItemStack getItem(int amount) {
        Tier tier = Tier.valueOf(this.name());
        String name = materialString;
        if (ordinal() < 2) {
            String[] split = materialString.split("/");
            name = split[1];
        }
        return new ItemWrapper()
                .setMaterial(material)
                .setName(Text.colorize(tier.getColorCode() + name + " Scrap"))
                .setLore(Text.colorize("&7Recovers 3% Durability of " + tier.getColorCode() + materialString + " &7Gear"))
                .setAmount(amount)
                .setTier(tier)
                .addKey(ARNamespacedKey.REAL_ITEM)
                .getItem();
    }

    public ItemStack getItem() {
        return getItem(1);
    }
}

package me.Lozke.data;

import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Pouch {
    T1(5),
    T2(25),
    T3(50),
    T4(75),
    T5(100),
    T6(5000);

    public static Pouch[] types = Pouch.values();

    private int maxGemAmount;

    Pouch(int maxGemAmount) {
        this.maxGemAmount = maxGemAmount;
    }

    public ItemStack getItem(int amount) {
        Tier tier = Tier.valueOf(this.name());
        return new ItemWrapper()
                .setMaterial(Material.INK_SAC)
                .setAmount(amount)
                .setName(tier.getColorCode() + tier.name() + " Pouch")
                .setLore(Text.colorize("&fhold gems and your gear i guess"))
                .setTier(tier)
                .addKey(ARNamespacedKey.REAL_ITEM)
                .addKey(ARNamespacedKey.GEM_WORTH, 0)
                .addKey(ARNamespacedKey.MAX_GEM_WORTH, maxGemAmount)
                .getItem();
    }

    public ItemStack getItem() {
        return getItem(1);
    }
}

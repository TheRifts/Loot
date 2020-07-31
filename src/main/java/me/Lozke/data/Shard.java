package me.Lozke.data;

import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Shard {
    T1(),
    T2(),
    T3(),
    T4(),
    T5(),
    T6();

    public static Shard[] types = Shard.values();

    public ItemStack getItem(int amount) {
        Tier tier = Tier.valueOf(this.name());
        return new ItemWrapper()
                .setMaterial(Material.BLAZE_POWDER)
                .setAmount(amount)
                .setName(tier.getColorCode() + tier.name() + " Shard of Alteration")
                .setLore(Text.colorize("&7Place on equipment to " + tier.getColorCode() + "&nrandomize&7 all bonus stats"))
                .setTier(tier)
                .addKey(ARNamespacedKey.REAL_ITEM)
                .getItem();
    }

    public ItemStack getItem() {
        return getItem(1);
    }
}

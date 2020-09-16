package me.Lozke.items;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.Tier;
import me.Lozke.utils.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Orb {
    T1(),
    T2(),
    T3(),
    T4(),
    T5(),
    T6();

    public static Orb[] types = Orb.values();

    public ItemStack getItem(int amount) {
        Tier tier = Tier.valueOf(this.name());
        return new ItemWrapper(Material.MAGMA_CREAM)
                .setAmount(amount)
                .setName(tier.getColorCode() + tier.name() + " Orb of Alteration")
                .setLore(Text.colorize("&7Place on equipment to " + tier.getColorCode() + "&nrandomize&7 all bonus attributes"))
                .setTier(tier)
                .addKey(ARNamespacedKey.REAL_ITEM)
                .getItem();
    }

    public ItemStack getItem() {
        return getItem(1);
    }
}

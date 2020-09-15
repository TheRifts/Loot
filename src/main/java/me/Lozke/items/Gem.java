package me.Lozke.items;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Gem {

    public static ItemStack getItem(int amount) {
        return new ItemWrapper(Material.EMERALD)
                .setAmount(amount)
                .setName("&aAgorian Gem")
                .setLore(Text.colorize("&7The premier currency of the Agorian Rifts"))
                .addKey(ARNamespacedKey.GEM_WORTH, 1)
                .addKey(ARNamespacedKey.REAL_ITEM)
                .getItem();
    }

    public static ItemStack getItem() {
        return getItem(1);
    }

}

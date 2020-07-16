package me.Lozke.data;

import me.Lozke.managers.ItemHandler;
import me.Lozke.utils.Items;
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

    public ItemStack getItem() {
        return ItemHandler.setTier(Items.formatItem(
                new ItemStack(Material.MAGMA_CREAM),
                Tier.valueOf(this.name()).getColorCode() +
                        this.name() +
                        " Orb of Alteration",
                new String[]{(Text.colorize("&7Place on equipment to " +
                        Tier.valueOf(this.name()).getColorCode() +
                        "&nrandomize&7 all bonus attributes"))}),
                Tier.valueOf(this.name()));
    }
}

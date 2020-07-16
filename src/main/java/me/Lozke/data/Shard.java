package me.Lozke.data;

import me.Lozke.managers.ItemHandler;
import me.Lozke.utils.Items;
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

    public ItemStack getItem() {
        return ItemHandler.setTier(Items.formatItem(new ItemStack(Material.BLAZE_POWDER),
                Tier.valueOf(this.name()).getColorCode() +
                        this.name() +
                        " Shard of Alteration",
                new String[]{(Text.colorize("&7Place on equipment to " +
                        Tier.valueOf(this.name()).getColorCode() +
                        "&nrandomize&7 all bonus stats"))}),
                Tier.valueOf(this.name()));
    }
}

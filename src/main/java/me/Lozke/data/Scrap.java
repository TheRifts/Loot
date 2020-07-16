package me.Lozke.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Scrap {
    T1(new ItemStack(Material.LEATHER)),
    T2(new ItemStack(Material.IRON_BARS)),
    T3(new ItemStack(Material.LIGHT_GRAY_DYE)),
    T4(new ItemStack(Material.LIGHT_BLUE_DYE)),
    T5(new ItemStack(Material.YELLOW_DYE)),
    T6(new ItemStack(Material.BLACK_DYE));

    public static Scrap[] types = Scrap.values();

    private final ItemStack item;

    Scrap(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}

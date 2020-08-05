package me.Lozke.menus;

import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class RepairSelector extends ItemMenu {

    private ItemStack item;

    public RepairSelector(ItemStack item) {
        super(InventoryType.HOPPER, "Select Repair Durability Amount");
        addDisplayItem(new RepairIcon(Items.formatItem(Material.RED_CONCRETE, Text.colorize("&f25%")), 0.25));
        addDisplayItem(new RepairIcon(Items.formatItem(Material.ORANGE_CONCRETE, Text.colorize("&f50%")), 0.50));
        addDisplayItem(new RepairIcon(Items.formatItem(Material.YELLOW_CONCRETE, Text.colorize("&f75%")), 0.75));
        addDisplayItem(new RepairIcon(Items.formatItem(Material.GREEN_CONCRETE, Text.colorize("&f100%")), 1));
        addDisplayItem(Items.formatItem(Material.WRITABLE_BOOK, Text.colorize("&fChoose a custom amount")));
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}

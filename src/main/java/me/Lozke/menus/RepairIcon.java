package me.Lozke.menus;

import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.CloseIcon;
import org.bukkit.inventory.ItemStack;

public class RepairIcon extends CloseIcon {

    double repairAmount;

    public RepairIcon(ItemStack icon, double repairAmount) {
        super(icon);
        this.repairAmount = repairAmount;
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        super.onItemClick(event);
        ItemWrapper itemWrapper = new ItemWrapper(((RepairSelector) event.getMenu()).getItem());
        double newDura = repairAmount + itemWrapper.getDurabalityAsPercentage();
        itemWrapper.setDurabalityAsPercentage(newDura);
    }
}

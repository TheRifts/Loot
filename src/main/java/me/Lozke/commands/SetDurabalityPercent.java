package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@CommandAlias("setdurabalitypercent")
public class SetDurabalityPercent extends BaseCommand {

    @Default
    public boolean execute(Player player, @Default("1.0") Double percent) {
        if (player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(Text.colorize("Must be holding an item"));
            return true;
        }
        ItemWrapper itemWrapper = new ItemWrapper(player.getItemInHand());
        if (!itemWrapper.isRealItem()) {
            return true;
        }
        if (percent < 0) {
            player.sendMessage("cannot be negative.");
            return true;
        }
        if (percent > 0.99) {
            percent = percent / 100;
        }
        itemWrapper.setDurabilityAsPercentage(percent);
        return true;
    }
}

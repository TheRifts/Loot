package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.utils.ItemWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("value")
public class ValueCommand extends BaseCommand {

    @Default
    public static void onValue(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemWrapper itemWrapper = new ItemWrapper(item);
        if (itemWrapper.isRealItem()) {
            player.sendMessage("value: " + itemWrapper.getInt(ARNamespacedKey.GEM_WORTH) * item.getAmount() + "g");
        }
        else {
            player.sendMessage("value: 0g");
        }
    }
}

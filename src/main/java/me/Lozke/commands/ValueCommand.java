package me.Lozke.commands;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.managers.ItemWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ValueCommand extends Command {

    public ValueCommand() {
        super("value");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemWrapper itemWrapper = new ItemWrapper(item);
        if (itemWrapper.isRealItem()) {
            player.sendMessage("value: " + itemWrapper.getInt(ARNamespacedKey.GEM_WORTH) * item.getAmount() + "g");
        }
        else {
            player.sendMessage("value: 0g");
        }
        return true;
    }
}

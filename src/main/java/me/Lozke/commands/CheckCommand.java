package me.Lozke.commands;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.Attribute;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CheckCommand extends Command {

    public CheckCommand() {
        super("verify");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemWrapper itemWrapper = new ItemWrapper(item);
        if (itemWrapper.isRealItem()) {
            player.sendMessage(itemWrapper.getString(ARNamespacedKey.REAL_ITEM));
            if (itemWrapper.hasKey(ARNamespacedKey.ATTRIBUTES)) {
                Map map = itemWrapper.getMap(ARNamespacedKey.ATTRIBUTES);
                for (Object key : map.keySet()) {
                    player.sendMessage(key + ": " + map.get(key) + " // " + ((double)(int)map.get(key) / Attribute.valueOf(String.valueOf(key)).getMaxValue()) * 100);
                }
            }
            if (itemWrapper.hasKey(ARNamespacedKey.HELD_ITEMS)) {
                Map heldItems = itemWrapper.getMap(ARNamespacedKey.HELD_ITEMS);
                for (Object key : heldItems.keySet()) {
                    if (heldItems.get(key) != null && ((byte[]) heldItems.get(key)).length > 0) {
                        ItemStack heldItem = ItemStack.deserializeBytes((byte[]) heldItems.get(key));
                        Logger.broadcast(heldItem.toString());
                    }
                }
            }
        }
        else {
            player.sendMessage("This is NOT a Agorian Rifts™ Item!");
        }
        return true;
    }
}

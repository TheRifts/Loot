package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.Attribute;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Logger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@CommandAlias("verify|check")
public class CheckCommand extends BaseCommand {

    @Default
    public static void onCheck(Player player) {
        ItemWrapper itemWrapper = new ItemWrapper(player.getInventory().getItemInMainHand());
        if (itemWrapper.isRealItem()) {
            player.sendMessage(itemWrapper.getString(ARNamespacedKey.REAL_ITEM));
            if (itemWrapper.hasKey(ARNamespacedKey.ATTRIBUTES)) {
                Map map = itemWrapper.getMap(ARNamespacedKey.ATTRIBUTES);
                for (Object key : map.keySet()) {
                    player.sendMessage(key + ": " + map.get(key));
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
    }
}

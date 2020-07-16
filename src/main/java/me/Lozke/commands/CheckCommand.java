package me.Lozke.commands;

import me.Lozke.data.Attribute;
import me.Lozke.data.NamespacedKeys;
import me.Lozke.managers.ItemHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class CheckCommand extends Command {

    public CheckCommand() {
        super("verify");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        boolean isRealItem = ItemHandler.isRealItem(item);
        if(isRealItem) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            player.sendMessage(container.get(NamespacedKeys.realItem, PersistentDataType.STRING));
            if (container.has(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE)) {
                Map map = container.get(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE);
                for (Object key : map.keySet()) {
                    player.sendMessage(key + ": " + map.get(key) + " // " + ((double)(int)map.get(key) / Attribute.valueOf(String.valueOf(key)).getMaxValue()) * 100);
                }
            }
            return true;
        }
        else {
            player.sendMessage("This is NOT a Agorian Rifts™ Item!");
        }
        return isRealItem;
    }
}

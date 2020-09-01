package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.Lozke.LootPlugin;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.Scroll.Modifier;
import me.Lozke.data.Scroll.ScrollData;
import me.Lozke.managers.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@CommandAlias("verify|check")
public class CheckCommand extends BaseCommand {

    @Default
    public static void onCheck(Player player) {
        ItemWrapper itemWrapper = new ItemWrapper(player.getInventory().getItemInMainHand());
        if (itemWrapper.isRealItem()) {
            player.sendMessage(itemWrapper.getBoolean(ARNamespacedKey.REAL_ITEM) + " ");
            if (itemWrapper.hasKey(ARNamespacedKey.ATTRIBUTES)) {
                Map map = itemWrapper.getMap(ARNamespacedKey.ATTRIBUTES);
                for (Object key : map.keySet()) {
                    player.sendMessage(key + ": " + map.get(key));
                }
            }
            if (itemWrapper.hasKey(ARNamespacedKey.SCROLL_MAX_AMOUNT)) {
                player.sendMessage("Max Scrolls: " + itemWrapper.get(ARNamespacedKey.SCROLL_MAX_AMOUNT));
            }
            if (itemWrapper.hasKey(ARNamespacedKey.HELD_ITEMS)) {
                Map heldItems = itemWrapper.getMap(ARNamespacedKey.HELD_ITEMS);
                for (Object key : heldItems.keySet()) {
                    if (heldItems.get(key) != null && ((byte[]) heldItems.get(key)).length > 0) {
                        ItemStack heldItem = ItemStack.deserializeBytes((byte[]) heldItems.get(key));
                        player.sendMessage(heldItem.toString());
                    }
                }
            }
            if (itemWrapper.hasKey(ARNamespacedKey.USED_SCROLLS)) {
                player.sendMessage("Max Amount of Scrolls: " + itemWrapper.getInt(ARNamespacedKey.SCROLL_MAX_AMOUNT));
                List<ScrollData> list = (List<ScrollData>) itemWrapper.getList(ARNamespacedKey.USED_SCROLLS);
                if (list != null) {
                    player.sendMessage("Amount of Applied Scrolls: " + list.size());
                    for (ScrollData scroll : list) {
                        player.sendMessage("Success Chance: " + scroll.getSuccessPercent());
                        player.sendMessage("Destroy Chance: " + scroll.getDestroyPercent());
                        player.sendMessage("Scroll Type: " + scroll.getScrollType());
                        player.sendMessage("Item Type to Enchant: " + scroll.getItemTypeToModify());
                        Map<Modifier, Object> modifierMap = scroll.getScrollData();
                        for (Modifier key : modifierMap.keySet()) {
                            player.sendMessage("Modifier: " + key + " Value: " + modifierMap.get(key));
                        }
                    }
                }
            }
        }
        else {
            player.sendMessage("This is NOT a Agorian Rifts™ Item!");
        }
    }

    @Subcommand("refresh")
    public static void onRefresh(Player player) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack == null || stack.getType() == Material.AIR) return;
        new ItemWrapper(stack).format();
    }

    @Subcommand("values")
    public static void onValueCheck() {
        LootPlugin.getPluginInstance().getItemFactoryInstance().showValues();
    }
}

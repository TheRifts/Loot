package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.Lozke.LootPlugin;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.items.Scroll.Modifier;
import me.Lozke.items.Scroll.ScrollData;
import me.Lozke.items.Scroll.ScrollDataTag;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@CommandAlias("verify|check")
public class CheckCommand extends BaseCommand {

    @Default
    public static void onCheck(Player player) {
        ItemWrapper itemWrapper = new ItemWrapper(player.getInventory().getItemInMainHand());
        if (itemWrapper.isRealItem()) {
            player.sendMessage("This is a Rifts™ Item!");
            if (itemWrapper.hasKey(ARNamespacedKey.MAJOR_STATS)) {
                player.sendMessage("Major Stats:");
                Map map = itemWrapper.getMap(ARNamespacedKey.MAJOR_STATS);
                for (Object key : map.keySet()) {
                    player.sendMessage(key + ": " + map.get(key));
                }
            }
            if (itemWrapper.hasKey(ARNamespacedKey.MINOR_STATS)) {
                player.sendMessage("Minor Stats:");
                Map map = itemWrapper.getMap(ARNamespacedKey.MINOR_STATS);
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
            if (itemWrapper.hasKey(ARNamespacedKey.UUID)) {
                player.sendMessage("Item UUID: " + itemWrapper.get(ARNamespacedKey.UUID));
            }
        }
        else {
            player.sendMessage("This is NOT a Rifts™ Item!");
        }
    }

    @Subcommand("scroll")
    public static void onCheckScroll(Player player) {
        LootPlugin plugin = LootPlugin.getPluginInstance();
        NamespacedKeyWrapper wrapper = new ItemWrapper(player.getInventory().getItemInMainHand());
        if (wrapper.hasKey(ScrollDataTag.DATA_TAG, new ScrollDataTag())) {
            ScrollData data = (ScrollData) wrapper.get(ScrollDataTag.DATA_TAG, new ScrollDataTag());
            player.sendMessage("Success Chance: " + data.getSuccessPercent());
            player.sendMessage("Destroy Chance: " + data.getDestroyPercent());
            player.sendMessage("Scroll Type: " + data.getScrollType());
            player.sendMessage("Item Type to Enchant: " + data.getItemTypeToModify());
            Map<Modifier, Object> modifierMap = data.getScrollData();
            for (Modifier key : modifierMap.keySet()) {
                player.sendMessage("Modifier: " + key + " Value: " + modifierMap.get(key));
            }
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

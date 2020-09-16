package me.Lozke.managers;

import me.Lozke.utils.ItemWrapper;
import me.Lozke.utils.Logger;
import me.Lozke.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnvilManager {

    enum AnvilManagerValue {
        ITEMSTACK,
        ITEM
    }

    private       final Map<UUID, Map<AnvilManagerValue, Object>> trackedPlayerData = new HashMap<>();
    public static final String INPUT_INSTRUCTIONS = Text.colorize("&7Type &a&lY&7 to confirm this repair. Type &c&lN&7 to cancel.");

    public void trackPlayer(UUID uuid, ItemStack itemStack, Item itemEntity) {
        if (trackedPlayerData.containsKey(uuid)) {
            Logger.log("Attempted to add uuid to anvil repair tracker (" + uuid + ") but they were already in set");
            return;
        }
        HashMap<AnvilManagerValue, Object> values = new HashMap<>();
        values.put(AnvilManagerValue.ITEMSTACK, itemStack);
        values.put(AnvilManagerValue.ITEM, itemEntity);
        trackedPlayerData.put(uuid, values);
    }
    public void trackPlayer(Player player, ItemStack itemStack, Item itemEntity) {
        trackPlayer(player.getUniqueId(), itemStack, itemEntity);
    }

    public void handleEvent(PlayerInteractEvent event) {
        ItemStack playerItem = event.getPlayer().getInventory().getItemInMainHand();

        ItemWrapper itemWrapper = new ItemWrapper(playerItem);
        if (!itemWrapper.isRealItem()) return;
        else event.getPlayer().getInventory().remove(playerItem);

        Location location = event.getClickedBlock().getLocation().add(.5, 1.1, .5);
        Item itemEntity = location.getWorld().dropItem(location, playerItem);
        itemEntity.setVelocity(new Vector());
        itemEntity.setPickupDelay(Integer.MAX_VALUE);

        event.getPlayer().sendMessage(Text.colorize("&eIt will cost you &a&l" + "0" + "G &eto repair your '" + itemWrapper.getItem().getItemMeta().getDisplayName() + "&a'"));
        event.getPlayer().sendMessage(INPUT_INSTRUCTIONS);

        trackPlayer(event.getPlayer().getUniqueId(), playerItem, itemEntity);
    }

    public void stopTracking(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            ItemStack item = getItemStack(uuid);
            player.getInventory().addItem(item);
            getItemEntity(uuid).remove();
        }
        trackedPlayerData.remove(uuid);
    }
    public void stopTracking(Player player) {
        stopTracking(player.getUniqueId());
    }

    public boolean isTracked(UUID uuid) {
        return trackedPlayerData.containsKey(uuid);
    }
    public boolean isTracked(Player player) {
        return isTracked(player.getUniqueId());
    }

    private ItemStack getItemStack(UUID uuid) {
        HashMap<AnvilManagerValue, Object> dataValues = (HashMap<AnvilManagerValue, Object>) trackedPlayerData.get(uuid);
        if (dataValues.containsKey(AnvilManagerValue.ITEMSTACK)) {
            return (ItemStack) dataValues.get(AnvilManagerValue.ITEMSTACK);
        }
        Logger.log("[AnvilManager] Attempted to get item for " + Bukkit.getPlayer(uuid) + " but item could not be found");
        return null;
    }

    private Item getItemEntity(UUID uuid) {
        HashMap<AnvilManagerValue, Object> dataValues = (HashMap<AnvilManagerValue, Object>) trackedPlayerData.get(uuid);
        return (Item) dataValues.get(AnvilManagerValue.ITEM);
    }

    public void repairItem(UUID uuid) {
        ItemWrapper itemWrapper = new ItemWrapper(getItemStack(uuid));
        itemWrapper.setDurabilityAsPercentage(1);
        Bukkit.getPlayer(uuid).sendMessage(Text.colorize("&c&l-0G"));
        stopTracking(uuid);
    }
    public void repairItem(Player player) {
        repairItem(player.getUniqueId());
    }
}

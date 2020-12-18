package me.Lozke.managers;

import me.Lozke.data.RepairContainer;
import me.Lozke.utils.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnvilManager {

    public enum RepairType {
        HAND,
        SET
    }

    private       final Map<UUID, RepairContainer> repairingContainerMap = new HashMap<>();
    public static final String INPUT_INSTRUCTIONS = Text.colorize("&7Type &a&lY&7 to confirm this repair. Type &c&lN&7 to cancel.");
    public final static EquipmentSlot[] slots = EquipmentSlot.values();

    //Afterthought: I am absolutely disgusted with this being here
    public void handleEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isTracked(player)) {
            player.sendMessage(Text.colorize("&cYou're already using an anvil"));
            return;
        }

        RepairType type = null;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (player.isSneaking()) {
                type = RepairType.SET;
            }
            else {
                type = RepairType.HAND;
            }
        }
        if (type == null) {
            return;
        }

        if (type == RepairType.HAND) {
            ItemWrapper wrapper = new ItemWrapper(event.getPlayer().getInventory().getItemInMainHand());
            if (!wrapper.isRealItem()) {
                return;
            }
            else if (wrapper.getDurabality() == wrapper.getMaxDurability()) {
                event.getPlayer().sendMessage(Text.colorize("&cThis item is already repaired."));
                return;
            }
        }
        else {
            boolean needsRepair = false;
            for (EquipmentSlot slot : slots) {
                if (slot == EquipmentSlot.OFF_HAND) continue;
                ItemWrapper wrapper = new ItemWrapper(player.getInventory().getItem(slot));
                if (!wrapper.isRealItem()) continue;
                if (wrapper.getDurabality() == wrapper.getMaxDurability()) continue;
                needsRepair = true;
                break;
            }
            if (!needsRepair) {
                player.sendMessage(Text.colorize("&cYour items are already repaired."));
                return;
            }
        }

        ItemStack stack = new ItemStack(Material.AIR);
        if (type == RepairType.HAND) {
            stack = player.getInventory().getItemInMainHand();
        }
        else {
            stack = new ItemStack(Material.ARMOR_STAND);
        }
        Location location = event.getClickedBlock().getLocation().add(.5, 1.1, .5);
        Item itemEntity = location.getWorld().dropItem(location, stack);
        itemEntity.setVelocity(new Vector());
        itemEntity.setPickupDelay(Integer.MAX_VALUE);

        RepairContainer container = new RepairContainer(player.getInventory(), type);

        container.setItem(itemEntity);

        repairingContainerMap.put(player.getUniqueId(), container);

        if (type == RepairType.HAND) {
            player.sendMessage(Text.colorize("&eIt will cost you &a&l" + calculateCost(container) + "G &eto repair your '" + stack.getItemMeta().getDisplayName() + "&a'"));
        }
        else {
            player.sendMessage(Text.colorize("&eIt will cost you &a&l" + calculateCost(container) + "G &eto repair your items"));
        }
        player.sendMessage(INPUT_INSTRUCTIONS);
    }

    public void stopTracking(UUID uuid) {
        RepairContainer container = repairingContainerMap.get(uuid);
        container.getItem().remove(); //Delete the item entity icon

        //Return the players gear
        Map<EquipmentSlot, ItemStack> items = container.getCachedItems();
        PlayerInventory inventory = Bukkit.getPlayer(uuid).getInventory();
        for (EquipmentSlot slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot));
        }

        repairingContainerMap.remove(uuid);
    }
    public void stopTracking(Player player) {
        stopTracking(player.getUniqueId());
    }

    public boolean isTracked(UUID uuid) {
        return repairingContainerMap.containsKey(uuid);
    }
    public boolean isTracked(Player player) {
        return isTracked(player.getUniqueId());
    }

    public void repair(UUID uuid) {
        Collection<ItemStack> items = repairingContainerMap.get(uuid).getCachedItems().values();
        for (ItemStack item : items) {
            ItemWrapper wrapper = new ItemWrapper(item);
            if (!wrapper.isRealItem()) continue;;
            wrapper.setDurabilityAsPercentage(1);
        }
        Bukkit.getPlayer(uuid).sendMessage(Text.colorize("&c&l-0G")); //TODO: Charge player here
        stopTracking(uuid);
    }
    public void repair(Player player) {
        repair(player.getUniqueId());
    }

    public int calculateCost(RepairContainer container) {
        return 0;
    }
}

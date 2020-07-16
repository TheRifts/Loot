package me.Lozke.commands;


import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.managers.ItemHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CreateItem extends Command {

    public CreateItem() {
        super("create");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        Inventory inv = player.getInventory();
        Tier tier = Tier.T1;
        String type = "";
        int amount = 1;
        Rarity rarity = Rarity.COMMON;

        if (args.length > 0) {
            type = args[0];
        }
        if (args.length > 1) {
            amount = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            tier = Tier.valueOf(args[2].toUpperCase());
        }
        if (args.length > 3) {
            rarity = Rarity.valueOf(args[3].toUpperCase());
        }

        switch (type) {
            case "gem":
                inv.addItem(ItemHandler.newGem(amount));
                return true;
            case "orb":
                inv.addItem(ItemHandler.newOrb(tier, amount));
                return true;
            case "shard":
                inv.addItem(ItemHandler.newShard(tier, amount));
                return true;
            case "scrap":
                inv.addItem(ItemHandler.newScrap(tier, amount));
                return true;
            case "sword":
                inv.addItem(ItemHandler.newWeapon(tier, rarity, "SWORD"));
                return true;
            case "axe":
                inv.addItem(ItemHandler.newWeapon(tier, rarity, "AXE"));
                return true;
            case "shovel":
                inv.addItem(ItemHandler.newWeapon(tier, rarity, "SHOVEL"));
                return true;
            case "hoe":
                inv.addItem(ItemHandler.newWeapon(tier, rarity, "HOE"));
                return true;
            case "boots":
                inv.addItem(ItemHandler.newBoots(tier,  rarity));
                return true;
            case "leggings":
                inv.addItem(ItemHandler.newLeggings(tier,  rarity));
                return true;
            case "chestplate":
                inv.addItem(ItemHandler.newChestplate(tier,  rarity));
                return true;
            case "helmet":
                inv.addItem(ItemHandler.newHelmet(tier,  rarity));
                return true;
            default:
                for (ItemStack item : ItemHandler.newSet(tier, rarity)) {
                    inv.addItem(item);
                }
                return true;
        }
    }
}

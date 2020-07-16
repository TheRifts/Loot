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
                inv.setItem(inv.firstEmpty(), ItemHandler.newGem(amount));
                return true;
            case "orb":
                inv.setItem(inv.firstEmpty(), ItemHandler.newOrb(tier, amount));
                return true;
            case "shard":
                inv.setItem(inv.firstEmpty(), ItemHandler.newShard(tier, amount));
                return true;
            case "scrap":
                inv.setItem(inv.firstEmpty(), ItemHandler.newScrap(tier, amount));
                return true;
            case "sword":
                inv.setItem(inv.firstEmpty(), ItemHandler.newWeapon(tier, rarity, "SWORD"));
                return true;
            case "axe":
                inv.setItem(inv.firstEmpty(), ItemHandler.newWeapon(tier, rarity, "AXE"));
                return true;
            case "shovel":
                inv.setItem(inv.firstEmpty(), ItemHandler.newWeapon(tier, rarity, "SHOVEL"));
                return true;
            case "hoe":
                inv.setItem(inv.firstEmpty(), ItemHandler.newWeapon(tier, rarity, "HOE"));
                return true;
            case "boots":
                inv.setItem(inv.firstEmpty(), ItemHandler.newBoots(tier,  rarity));
                return true;
            case "leggings":
                inv.setItem(inv.firstEmpty(), ItemHandler.newLeggings(tier,  rarity));
                return true;
            case "chestplate":
                inv.setItem(inv.firstEmpty(), ItemHandler.newChestplate(tier,  rarity));
                return true;
            case "helmet":
                inv.setItem(inv.firstEmpty(), ItemHandler.newHelmet(tier,  rarity));
                return true;
            default:
                for (ItemStack item : ItemHandler.newSet(tier,  rarity)) {
                    inv.setItem(inv.firstEmpty(), item);
                }
                return true;
        }
    }
}

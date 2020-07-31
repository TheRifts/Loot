package me.Lozke.commands;


import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.managers.ItemFactory;
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
            case "gemnote":
                // amount = value
                inv.addItem(ItemFactory.newGemNote(amount));
                return true;
            case "pouch":
                inv.addItem(ItemFactory.newPouch(tier, amount));
                return true;
            case "gem":
                inv.addItem(ItemFactory.newGem(amount));
                return true;
            case "orb":
                inv.addItem(ItemFactory.newOrb(tier, amount));
                return true;
            case "shard":
                inv.addItem(ItemFactory.newShard(tier, amount));
                return true;
            case "scrap":
                inv.addItem(ItemFactory.newScrap(tier, amount));
                return true;
            case "sword":
                inv.addItem(ItemFactory.newWeapon(tier, rarity, "SWORD"));
                return true;
            case "axe":
                inv.addItem(ItemFactory.newWeapon(tier, rarity, "AXE"));
                return true;
            case "shovel":
                inv.addItem(ItemFactory.newWeapon(tier, rarity, "SHOVEL"));
                return true;
            case "hoe":
                inv.addItem(ItemFactory.newWeapon(tier, rarity, "HOE"));
                return true;
            case "boots":
                inv.addItem(ItemFactory.newBoots(tier,  rarity));
                return true;
            case "leggings":
                inv.addItem(ItemFactory.newLeggings(tier,  rarity));
                return true;
            case "chestplate":
                inv.addItem(ItemFactory.newChestplate(tier,  rarity));
                return true;
            case "helmet":
                inv.addItem(ItemFactory.newHelmet(tier,  rarity));
                return true;
            default:
                for (ItemStack item : ItemFactory.newSet(tier, rarity)) {
                    inv.addItem(item);
                }
                return true;
        }
    }
}

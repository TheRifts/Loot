package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Lozke.data.*;
import me.Lozke.data.Scroll.ScrollType;
import me.Lozke.managers.ItemFactory;
import me.Lozke.utils.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("create")
public class CreateItem extends BaseCommand {

    @Subcommand("set")
    @Syntax("tier rarity")
    @CommandCompletion("@tier @rarity")
    public static void createSet(Player player, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        for (ItemStack item : ItemFactory.newSet(tier, rarity)) {
            player.getInventory().addItem(item);
        }
    }

    @Subcommand("armour")
    @Syntax("type tier rarity")
    @CommandCompletion("@armour-type @tier @rarity")
    public static void createArmour(Player player, ArmourType armourType, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        player.getInventory().addItem(ItemFactory.newArmour(tier, rarity, armourType));
    }

    @Subcommand("weapon")
    @Syntax("type tier rarity")
    @CommandCompletion("@weapon-type @tier @rarity")
    public static void createWeapon(Player player, @Default("SWORD") WeaponType wepType, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        player.getInventory().addItem(ItemFactory.newWeapon(tier, rarity, wepType));
    }

    @Subcommand("scrap")
    @Syntax("tier amount")
    @CommandCompletion("@tier")
    public static void createScrap(Player player, @Default("T1") Tier tier, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newScrap(tier, amount));
    }

    @Subcommand("shard")
    @Syntax("tier amount")
    @CommandCompletion("@tier")
    public static void createShard(Player player, @Default("T1") Tier tier, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newShard(tier, amount));
    }

    @Subcommand("orb")
    @Syntax("tier amount")
    @CommandCompletion("@tier")
    public static void createOrb(Player player, @Default("T1") Tier tier, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newOrb(tier, amount));
    }

    @Subcommand("pouch")
    @Syntax("tier amount")
    @CommandCompletion("@tier")
    public static void createPouch(Player player, @Default("T1") Tier tier, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newPouch(tier, amount));
    }

    @Subcommand("gem")
    @Syntax("[amount]")
    public static void createGem(Player player, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newGem(amount));
    }

    @Subcommand("gemnote|note")
    @Syntax("[value]")
    public static void createGemNote(Player player, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newGemNote(amount));
    }

    @Subcommand("scroll")
    @Syntax("scrolltype itemtype [success-chance] [destroy-chance] <modifier,modifieramount>")
    public static void createScroll(Player player, ScrollType scrollType, ItemType itemType, @Default("1") Double successChance, @Default("0") Double destroyChance, @Split(",| ") String[] args) {
        if (args.length % 2 != 0) {
            player.sendMessage(Text.colorize("&cInvalid Scroll Modifer/Amount Combination)"));
            return;
        }
        player.getInventory().addItem(ItemFactory.newScroll(scrollType, itemType, successChance, destroyChance, args));
    }
}

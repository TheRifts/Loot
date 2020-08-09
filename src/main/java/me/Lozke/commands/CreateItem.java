package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.managers.ItemFactory;
import me.Lozke.utils.Logger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("create")
public class CreateItem extends BaseCommand {

    @Subcommand("set")
    @Syntax("tier rarity")
    public static void createSet(Player player, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        for (ItemStack item : ItemFactory.newSet(tier, rarity)) {
            player.getInventory().addItem(item);
        }
    }

    @Subcommand("helmet|helm")
    @Syntax("tier rarity")
    public static void createHelmet(Player player, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        player.getInventory().addItem(ItemFactory.newHelmet(tier, rarity));
    }

    @Subcommand("chestplate|chest")
    @Syntax("tier rarity")
    public static void createChestplate(Player player, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        player.getInventory().addItem(ItemFactory.newChestplate(tier, rarity));
    }

    @Subcommand("leggings|leg")
    @Syntax("tier rarity")
    public static void createLeggings(Player player, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        player.getInventory().addItem(ItemFactory.newLeggings(tier, rarity));
    }

    @Subcommand("boots")
    @Syntax("tier rarity")
    public static void createBoots(Player player, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        player.getInventory().addItem(ItemFactory.newBoots(tier, rarity));
    }

    @Subcommand("weapon")
    @Syntax("type tier rarity")
    @CommandCompletion("sword|axe|shovel|hoe")
    public static void createWeapon(Player player, String wepType, @Default("T1") Tier tier, @Default("COMMON") Rarity rarity) {
        player.getInventory().addItem(ItemFactory.newWeapon(tier, rarity, wepType));
    }

    @Subcommand("scrap")
    @Syntax("tier amount")
    public static void createScrap(Player player, @Default("T1") Tier tier, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newScrap(tier, amount));
    }

    @Subcommand("shard")
    @Syntax("tier amount")
    public static void createShard(Player player, @Default("T1") Tier tier, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newShard(tier, amount));
    }

    @Subcommand("orb")
    @Syntax("tier amount")
    public static void createOrb(Player player, @Default("T1") Tier tier, @Default("1") Integer amount) {
        player.getInventory().addItem(ItemFactory.newOrb(tier, amount));
    }

    @Subcommand("pouch")
    @Syntax("tier amount")
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
}

package me.Lozke.managers;

import me.Lozke.data.*;
import me.Lozke.data.Scroll.Modifier;
import me.Lozke.data.Scroll.ScrollData;
import me.Lozke.data.Scroll.ScrollType;
import me.Lozke.utils.*;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

public class ItemFactory {

    //lol this would be the perfect place to return a Set<ItemStack>... just saying...
    //the prophecy has come true
    public static Set<ItemStack> newSet(Tier tier, Rarity rarity) {
        Set<ItemStack> stackSet = new HashSet<>();
        for (ArmourType type : ArmourType.types) {
            stackSet.add(type.getItem(tier));
        }
        return stackSet;
    }

    public static ItemStack newArmour(Tier tier, Rarity rarity, EquipmentSlot slot) {
        ArmourType armourType = ArmourType.fromEquipmentSlot(slot);
        if (armourType == null) return null;
        return newArmour(tier, rarity, armourType);
    }
    public static ItemStack newArmour(Tier tier, Rarity rarity, ArmourType gearType) {
        return createItem(tier, rarity, gearType.getItem(tier), ItemType.ARMOR);
    }

    public static ItemStack newWeapon(Tier tier, Rarity rarity, WeaponType weaponType) {
        return createItem(tier, rarity, weaponType.getItem(tier), ItemType.WEAPON);
    }

    public static ItemStack newScrap(Tier tier) {
        return newScrap(tier, 1);
    }
    public static ItemStack newScrap(Tier tier, int amount) {
        return Scrap.types[tier.ordinal()].getItem(amount);
    }

    public static ItemStack newOrb(Tier tier) {
        return newOrb(tier, 1);
    }
    public static ItemStack newOrb(Tier tier, int amount) {
        return Orb.types[tier.ordinal()].getItem(amount);
    }

    public static ItemStack newShard(Tier tier) {
        return newShard(tier, 1);
    }
    public static ItemStack newShard(Tier tier, int amount) {
        return Shard.types[tier.ordinal()].getItem(amount);
    }

    public static ItemStack newGem() {
        return newGem(1);
    }
    public static ItemStack newGem(int amount) {
        return Gem.getItem(amount);
    }

    public static ItemStack newPouch(Tier tier) {
        return newPouch(tier,1);
    }
    public static ItemStack newPouch(Tier tier, int amount) {
        return Pouch.types[tier.ordinal()].getItem(amount);
    }

    public static ItemStack newGemNote(int value) {
        return new ItemWrapper(Material.PAPER)
                .setAmount(1)
                .setName(Text.colorize("&aAgorian Gem Note"))
                .setLore(Text.colorize("&fValue: &a" + value))
                .addKey(ARNamespacedKey.REAL_ITEM)
                .addKey(ARNamespacedKey.GEM_WORTH, value)
                .getItem();
    }

    public static ItemStack newScroll(ScrollType scrollType, ItemType itemType, double successChance, double destroyChance, String[] modifierArgs) {
        ScrollData scroll = new ScrollData();

        scroll.setScrollType(scrollType);
        scroll.setItemTypeToModify(itemType);
        scroll.setSuccessPercent(successChance);
        scroll.setDestroyPercent(destroyChance);

        for (int i = 0; i < modifierArgs.length; i = i+2) {
            if (i + 1 > modifierArgs.length) {
                break;
            }
            Modifier modifier = Modifier.valueOf(modifierArgs[i]);
            Double amount = Double.valueOf(modifierArgs[i+1]);
            scroll.addModifier(modifier, amount);
        }

        return scroll.getItem();
    }

    private static ItemStack createItem(Tier tier, Rarity rarity, ItemStack stack, ItemType itemType) {
        ItemWrapper itemWrapper = new ItemWrapper(stack);
        switch (itemType) {
            case ARMOR:
                if (NumGenerator.roll(2) == 1) {
                    itemWrapper.addKey(ARNamespacedKey.HP_REGEN, 50);
                }
                else {
                    itemWrapper.addKey(ARNamespacedKey.ENERGY_REGEN, 3);
                }
                //Hardcoding to 0 hp till fallen blesses the code with a better algorithm
                itemWrapper.addKey(ARNamespacedKey.HEALTH_POINTS, 0);
                break;
            case WEAPON:
                //Hardcoding to 1k dmg till fallen blesses the code with a better algorithm
                itemWrapper.addKey(ARNamespacedKey.DMG_LO, 1000);
                itemWrapper.addKey(ARNamespacedKey.DMG_HI, 1000);
                break;
        }

        itemWrapper.addKey(ARNamespacedKey.REAL_ITEM);
        itemWrapper.addKey(ARNamespacedKey.TIER, tier.name());
        itemWrapper.addKey(ARNamespacedKey.RARITY, rarity.name());
        itemWrapper.addKey(ARNamespacedKey.CAN_ORB);
        itemWrapper.addKey(ARNamespacedKey.DURABILITY, tier.getMaxDurability());
        itemWrapper.addKey(ARNamespacedKey.MAX_DURABILITY, tier.getMaxDurability());
        itemWrapper.addKey(ARNamespacedKey.SCROLL_MAX_AMOUNT, NumGenerator.roll(10));

        itemWrapper.randomizeAttributes();

        stack = itemWrapper.getItem();
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(itemMeta);
        return stack;
    }
}

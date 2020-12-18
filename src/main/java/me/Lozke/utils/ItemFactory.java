package me.Lozke.utils;

import me.Lozke.data.*;
import me.Lozke.items.Scroll.Modifier;
import me.Lozke.items.Scroll.ScrollData;
import me.Lozke.items.Scroll.ScrollType;
import me.Lozke.items.*;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemFactory {

    private static       ItemFactory instance;

    private static final int damageSeed = 200;
    private static final double tierIncrease = 2.0;
    private static final double tierGapScale= 1.2;
    private static final int hitsToKill = 75;
    private static final int defenseScale = 1000;
    private static final double[][] mobDropScaling = {
            {0.0, 0.25},
            {0.2, 0.5},
            {0.42, 0.82},
            {0.69, 1},
            {0.92, 1.5}
    };
    private static final int healTime = 25;

    private static       int[][][] weaponDamage;
    private static       int[][][] armourDefense;
    private static       int[][][] armourHP;
    private static       int[][][] armourHPRegen;

    private static       int[][][] mobHP;

    private static final int dropRateSeed = 25;
    private static       int[][] dropRate;

    private static final int[][] energy = {
            {1, 3},
            {2, 5},
            {3, 6},
            {4, 7},
            {5, 8}
    };


    public ItemFactory() {
        instance = this;

        int[][] damageRanges = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            if (tierIndex == 0) {
                damageRanges[tierIndex][0] = damageSeed;
            }
            else {
                damageRanges[tierIndex][0] = intCeiling(damageRanges[tierIndex-1][1]*tierGapScale);
            }
            damageRanges[tierIndex][1] = intCeiling(damageRanges[tierIndex][0]*tierIncrease);
        }

        weaponDamage = new int[5][5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                int low = damageRanges[tierIndex][0];
                int high = damageRanges[tierIndex][1];

                weaponDamage[tierIndex][rarityIndex][0] = intCeiling((high-low)*mobDropScaling[rarityIndex][0]+low);
                weaponDamage[tierIndex][rarityIndex][1] = intCeiling((high-low)*mobDropScaling[rarityIndex][1]+low);
            }
        }

        int[][] armourSetDefense = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            if (tier.ordinal() == 0) {
                armourSetDefense[tierIndex][0] = defenseScale/2+1;
            }
            else {
                armourSetDefense[tierIndex][0] = armourSetDefense[tierIndex-1][0]+defenseScale/2;
            }
            armourSetDefense[tierIndex][1] = armourSetDefense[tierIndex][0]+defenseScale/2-1;
        }

        int[][] defenseRanges = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            defenseRanges[tierIndex][0] = intCeiling(armourSetDefense[tierIndex][0]/4.0);
            defenseRanges[tierIndex][1] = intCeiling(armourSetDefense[tierIndex][1]/4.0);
        }

        armourDefense = new int[5][5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                int low = defenseRanges[tierIndex][0];
                int high = defenseRanges[tierIndex][1];

                armourDefense[tierIndex][rarityIndex][0] = intCeiling((high-low)*mobDropScaling[rarityIndex][0]+low);
                armourDefense[tierIndex][rarityIndex][1] = intCeiling((high-low)*mobDropScaling[rarityIndex][1]+low);
            }
        }

        int[][] armourSetHP = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            armourSetHP[tierIndex][0] = intCeiling(damageRanges[tierIndex][0]*hitsToKill*getDamageTaken(armourSetDefense[tierIndex][0]));
            armourSetHP[tierIndex][1] = intCeiling(damageRanges[tierIndex][1]*hitsToKill*getDamageTaken(armourSetDefense[tierIndex][1]));
        }

        int[][] hpRanges = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            hpRanges[tierIndex][0] = intCeiling(armourSetHP[tierIndex][0]/4.0);
            hpRanges[tierIndex][1] = intCeiling(armourSetHP[tierIndex][1]/4.0);
        }

        armourHP = new int[5][5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                int low = hpRanges[tierIndex][0];
                int high = hpRanges[tierIndex][1];

                armourHP[tierIndex][rarityIndex][0] = intCeiling((high-low)*mobDropScaling[rarityIndex][0]+low);
                armourHP[tierIndex][rarityIndex][1] = intCeiling((high-low)*mobDropScaling[rarityIndex][1]+low);
            }
        }

        armourHPRegen = new int[5][5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                armourHPRegen[tierIndex][rarityIndex][0] = intCeiling(armourHP[tierIndex][rarityIndex][0]/(healTime/4.0));
                armourHPRegen[tierIndex][rarityIndex][1] = intCeiling(armourHP[tierIndex][rarityIndex][1]/(healTime/4.0));
            }
        }

        dropRate = new int[5][5];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                if (tierIndex != 0 || rarityIndex != 0) {
                    if (rarityIndex != 0) {
                        dropRate[tierIndex][rarityIndex] = dropRate[tierIndex][rarityIndex - 1] + tierIndex + 2;
                    }
                    else {
                        dropRate[tierIndex][rarityIndex] = dropRate[tierIndex - 1][rarityIndex + 4] + (tierIndex + 2)*2;
                    }
                }
                else {
                    dropRate[tierIndex][rarityIndex] = dropRateSeed;
                }
            }
        }

        int[][] mobSetHP = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            mobSetHP[tierIndex][0] = intCeiling(damageRanges[tierIndex][0]*hitsToKill);
            mobSetHP[tierIndex][1] = intCeiling(damageRanges[tierIndex][1]*hitsToKill);
        }

        mobHP = new int[5][5][2];
        for (Tier tier : Tier.types) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.types) {
                int rarityIndex = rarity.ordinal();

                int low = mobSetHP[tierIndex][0];
                int high = mobSetHP[tierIndex][1];

                mobHP[tierIndex][rarityIndex][0] = intCeiling((high-low)*mobDropScaling[rarityIndex][0]+low);
                mobHP[tierIndex][rarityIndex][1] = intCeiling((high-low)*mobDropScaling[rarityIndex][1]+low);
            }
        }
    }
    private static int intCeiling(double value) {
        return (int)Math.ceil(value);
    }
    private static double getDamageTaken(int defense) {
        return 1-defense/(double)(defense+defenseScale);
    }
    public void showValues() {
        Logger.log("DAMAGE VALUES:");
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " damage: " + weaponDamage[tierIndex][rarityIndex][0] + " - " + weaponDamage[tierIndex][rarityIndex][1]);
            }
            Logger.log("");
        }
        Logger.log("");
        Logger.log("");
        Logger.log("DEFENSE VALUES:");
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " defense: " + armourDefense[tierIndex][rarityIndex][0] + " - " + armourDefense[tierIndex][rarityIndex][1]);
            }
            Logger.log("");
        }
        Logger.log("");
        Logger.log("");
        Logger.log("HP VALUES:");
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " hp: " + armourHP[tierIndex][rarityIndex][0] + " - " + armourHP[tierIndex][rarityIndex][1]);
            }
            Logger.log("");
        }
        Logger.log("");
        Logger.log("");
        Logger.log("Mob HP VALUES:");
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " hp: " + mobHP[tierIndex][rarityIndex][0] + " - " + mobHP[tierIndex][rarityIndex][1]);
            }
            Logger.log("");
        }
        Logger.log("");
        Logger.log("");
        Logger.log("HP/S VALUES:");
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " hp/s: " + armourHPRegen[tierIndex][rarityIndex][0] + " - " + armourHPRegen[tierIndex][rarityIndex][1]);
            }
            Logger.log("");
        }
        Logger.log("");
        Logger.log("");
        Logger.log("Drop rate VALUES:");
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " drop rate: " + dropRate[tierIndex][rarityIndex]);
            }
            Logger.log("");
        }
        Logger.log("");
        Logger.log("");
        Logger.log("Energy regen VALUES:");
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            Logger.log(tier.toString() + " energy regen: " + energy[tierIndex][0] + " - " + energy[tierIndex][1]);
        }
    }

    public static enum RangeType {
        LOW,
        HIGH
    }
    public static int getDamage(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 4) return 0;
        return weaponDamage[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getArmourDefense(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 4) return 0;
        return armourDefense[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getArmourHP(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 4) return 0;
        return armourHP[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getArmourHPRegen(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 4) return 0;
        return armourHPRegen[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getMobHP(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 4) return 0;
        return mobHP[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getArmourEnergyRegen(Tier tier, RangeType type) {
        if (tier.ordinal() > 4) return 0;
        return energy[tier.ordinal()][type.ordinal()];
    }

    //lol this would be the perfect place to return a Set<ItemStack>... just saying...
    //the prophecy has come true
    //the prophecy has been defeated by my OCD, can't stand the items showing up in random order
    public static List<ItemStack> newSet(Tier tier, Rarity rarity) {
        List<ItemStack> set = new ArrayList<>();
        set.add(newWeapon(tier, rarity));
        for (ArmourType type : ArmourType.types) {
            set.add(newArmour(tier, rarity, type));
        }
        return set;
    }

    public static ItemStack newArmour(Tier tier, Rarity rarity) {
        return newArmour(tier, rarity, ArmourType.getRandomArmourType());
    }
    public static ItemStack newArmour(Tier tier, Rarity rarity, EquipmentSlot slot) {
        ArmourType armourType = ArmourType.fromEquipmentSlot(slot);
        if (armourType == null) return null;
        return newArmour(tier, rarity, armourType);
    }
    public static ItemStack newArmour(Tier tier, Rarity rarity, ArmourType gearType) {
        return createItem(tier, rarity, gearType.getItem(tier), ItemType.ARMOUR);
    }

    public static ItemStack newWeapon(Tier tier, Rarity rarity) {
        return newWeapon(tier, rarity, WeaponType.getRandomWeaponType());
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
        scroll.setSuccessPercent((successChance > 0.99) ? successChance / 100 : successChance);
        scroll.setDestroyPercent((destroyChance > 0.99) ? destroyChance / 100 : destroyChance);

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
        int tierIndex = tier.ordinal();
        int rarityIndex = rarity.ordinal();
        HashMap<RiftsStat, Integer> majorStats = new HashMap<>();
        switch (itemType) {
            case ARMOUR:
                if (NumGenerator.roll(2) == 1) {
                    if (tierIndex < 5 && rarityIndex < 5) {
                        majorStats.put(RiftsStat.HP_REGEN, NumGenerator.rollInclusive(armourHPRegen[tierIndex][rarityIndex][0], armourHPRegen[tierIndex][rarityIndex][1]));
                    }
                    else {
                        majorStats.put(RiftsStat.HP_REGEN, 50);
                    }
                }
                else {
                    if (tierIndex < 5 && rarityIndex < 5) {
                        majorStats.put(RiftsStat.ENERGY_REGEN, NumGenerator.rollInclusive(energy[tierIndex][0], energy[tierIndex][1]));
                    }
                    else {
                        majorStats.put(RiftsStat.ENERGY_REGEN, 10);
                    }
                }
                if (tierIndex < 5 && rarityIndex < 5) {
                    majorStats.put(RiftsStat.HP, NumGenerator.rollInclusive(armourHP[tierIndex][rarityIndex][0], armourHP[tierIndex][rarityIndex][1]));
                }
                else {
                    //Hardcoding to 0 hp till fallen blesses the code with a better algorithm
                    majorStats.put(RiftsStat.HP, 0);
                }
                if (tierIndex < 5 && rarityIndex < 5) {
                    majorStats.put(RiftsStat.DEFENSE, NumGenerator.rollInclusive(armourDefense[tierIndex][rarityIndex][0], armourDefense[tierIndex][rarityIndex][1]));
                }
                else {
                    //Hardcoding to 0 hp till fallen blesses the code with a better algorithm
                    majorStats.put(RiftsStat.DEFENSE, 5);
                }
                break;
            case WEAPON:
                if (tierIndex < 5 && rarityIndex < 5) {
                    int DMG_LO = NumGenerator.rollInclusive(weaponDamage[tierIndex][rarityIndex][0], weaponDamage[tierIndex][rarityIndex][1]);
                    majorStats.put(RiftsStat.DMG_LO, DMG_LO);
                    majorStats.put(RiftsStat.DMG_HI, NumGenerator.rollInclusive(DMG_LO, weaponDamage[tierIndex][rarityIndex][1]));
                }
                else {
                    //Hardcoding to 1k dmg till fallen blesses the code with a better algorithm
                    majorStats.put(RiftsStat.DMG_LO, 1000);
                    majorStats.put(RiftsStat.DMG_HI, 1000);
                }
                break;
        }

        itemWrapper.addKey(ARNamespacedKey.MAJOR_STATS, majorStats);
        itemWrapper.addKey(ARNamespacedKey.REAL_ITEM);
        itemWrapper.addKey(ARNamespacedKey.TIER, tier.name());
        itemWrapper.addKey(ARNamespacedKey.RARITY, rarity.name());
        itemWrapper.addKey(ARNamespacedKey.CAN_ORB);
        itemWrapper.addKey(ARNamespacedKey.DURABILITY, tier.getMaxDurability());
        itemWrapper.addKey(ARNamespacedKey.MAX_DURABILITY, tier.getMaxDurability());
        itemWrapper.addKey(ARNamespacedKey.SCROLL_MAX_AMOUNT, NumGenerator.roll(10));

        itemWrapper.randomizeStats();

        stack = itemWrapper.getItem();
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public static int getKillsToDrop(Tier tier, Rarity rarity) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 3) {
            return 1;
        }
        double value = NumGenerator.fraction();
        int tierDropRate = dropRate[tier.ordinal()][rarity.ordinal()];
        int thresholdLow = tierDropRate/2;
        int thresholdMid = tierDropRate*5/4;
        int thresholdHigh = tierDropRate*8/3;
        if (value < 0.25) {
            return Math.max(1, NumGenerator.roll(thresholdLow));
        }
        else if (value < 0.75) {
            return Math.max(1, NumGenerator.rollInclusive(thresholdLow+1, thresholdMid));
        }
        else {
            return Math.max(1, NumGenerator.rollInclusive(thresholdMid+1, thresholdHigh));
        }
    }
}

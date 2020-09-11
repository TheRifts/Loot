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
            {0.69, 1}
    };
    private static final int healTime = 25;

    private static       int[][][] weaponDamage;
    private static       int[][][] armorDefense;
    private static       int[][][] armorHP;
    private static       int[][][] armorHPRegen;

    private static final int dropRateSeed = 10;
    private static       int[][] dropRate;


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

        weaponDamage = new int[5][4][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();
                if (rarityIndex>3) continue;

                int low = damageRanges[tierIndex][0];
                int high = damageRanges[tierIndex][1];

                weaponDamage[tierIndex][rarityIndex][0] = intCeiling((high-low)*mobDropScaling[rarityIndex][0]+low);
                weaponDamage[tierIndex][rarityIndex][1] = intCeiling((high-low)*mobDropScaling[rarityIndex][1]+low);
            }
        }

        int[][] armorSetDefense = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            if (tier.ordinal() == 0) {
                armorSetDefense[tierIndex][0] = defenseScale/2+1;
            }
            else {
                armorSetDefense[tierIndex][0] = armorSetDefense[tierIndex-1][0]+defenseScale/2;
            }
            armorSetDefense[tierIndex][1] = armorSetDefense[tierIndex][0]+defenseScale/2-1;
        }

        int[][] defenseRanges = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            defenseRanges[tierIndex][0] = intCeiling(armorSetDefense[tierIndex][0]/4.0);
            defenseRanges[tierIndex][1] = intCeiling(armorSetDefense[tierIndex][1]/4.0);
        }

        armorDefense = new int[5][4][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();
                if (rarityIndex>3) continue;

                int low = defenseRanges[tierIndex][0];
                int high = defenseRanges[tierIndex][1];

                armorDefense[tierIndex][rarityIndex][0] = intCeiling((high-low)*mobDropScaling[rarityIndex][0]+low);
                armorDefense[tierIndex][rarityIndex][1] = intCeiling((high-low)*mobDropScaling[rarityIndex][1]+low);
            }
        }

        int[][] armorSetHP = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            armorSetHP[tierIndex][0] = intCeiling(damageRanges[tierIndex][0]*hitsToKill*getDamageTaken(armorSetDefense[tierIndex][0]));
            armorSetHP[tierIndex][1] = intCeiling(damageRanges[tierIndex][1]*hitsToKill*getDamageTaken(armorSetDefense[tierIndex][1]));
        }

        int[][] hpRanges = new int[5][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;

            hpRanges[tierIndex][0] = intCeiling(armorSetHP[tierIndex][0]/4.0);
            hpRanges[tierIndex][1] = intCeiling(armorSetHP[tierIndex][1]/4.0);
        }

        armorHP = new int[5][4][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();
                if (rarityIndex>3) continue;

                int low = hpRanges[tierIndex][0];
                int high = hpRanges[tierIndex][1];

                armorHP[tierIndex][rarityIndex][0] = intCeiling((high-low)*mobDropScaling[rarityIndex][0]+low);
                armorHP[tierIndex][rarityIndex][1] = intCeiling((high-low)*mobDropScaling[rarityIndex][1]+low);
            }
        }

        armorHPRegen = new int[5][4][2];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();
                if (rarityIndex>3) continue;

                armorHPRegen[tierIndex][rarityIndex][0] = intCeiling(armorHP[tierIndex][rarityIndex][0]/(healTime/4.0));
                armorHPRegen[tierIndex][rarityIndex][1] = intCeiling(armorHP[tierIndex][rarityIndex][1]/(healTime/4.0));
            }
        }

        dropRate = new int[5][4];
        for (Tier tier : Tier.values()) {
            int tierIndex = tier.ordinal();
            if (tierIndex>4) continue;
            for (Rarity rarity : Rarity.values()) {
                int rarityIndex = rarity.ordinal();
                if (rarityIndex > 3) continue;

                if (tierIndex != 0 || rarityIndex != 0) {
                    if (rarityIndex != 0) {
                        dropRate[tierIndex][rarityIndex] = dropRate[tierIndex][rarityIndex - 1] + tierIndex + 2;
                    }
                    else {
                        dropRate[tierIndex][rarityIndex] = dropRate[tierIndex - 1][rarityIndex + 3] + (tierIndex + 2)*2;
                    }
                }
                else {
                    dropRate[tierIndex][rarityIndex] = dropRateSeed;
                }
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
                if (rarityIndex>3) continue;

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
                if (rarityIndex>3) continue;

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " defense: " + armorDefense[tierIndex][rarityIndex][0] + " - " + armorDefense[tierIndex][rarityIndex][1]);
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
                if (rarityIndex>3) continue;

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " hp: " + armorHP[tierIndex][rarityIndex][0] + " - " + armorHP[tierIndex][rarityIndex][1]);
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
                if (rarityIndex>3) continue;

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " hp/s: " + armorHPRegen[tierIndex][rarityIndex][0] + " - " + armorHPRegen[tierIndex][rarityIndex][1]);
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
                if (rarityIndex>3) continue;

                Logger.log(tier.toString() + " level " + (rarity.ordinal()+1) + " drop rate: " + dropRate[tierIndex][rarityIndex]);
            }
            Logger.log("");
        }
    }

    public static enum RangeType {
        LOW,
        HIGH
    }
    public static int getDamage(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 3) return 0;
        return weaponDamage[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getArmourDefense(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 3) return 0;
        return armorDefense[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getArmourHP(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 3) return 0;
        return armorHP[tier.ordinal()][rarity.ordinal()][type.ordinal()];
    }
    public static int getArmourHPRegen(Tier tier, Rarity rarity, RangeType type) {
        if (tier.ordinal() > 4 || rarity.ordinal() > 3) return 0;
        return armorHPRegen[tier.ordinal()][rarity.ordinal()][type.ordinal()];
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
        return createItem(tier, rarity, gearType.getItem(tier), ItemType.ARMOR);
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
        int tierIndex = tier.ordinal();
        int rarityIndex = rarity.ordinal();
        HashMap<RiftsStat, Integer> majorStats = new HashMap<>();
        switch (itemType) {
            case ARMOR:
                if (NumGenerator.roll(2) == 1) {
                    if (tierIndex < 5 && rarityIndex < 4) {
                        majorStats.put(RiftsStat.HP_REGEN, NumGenerator.rollInclusive(armorHPRegen[tierIndex][rarityIndex][0], armorHPRegen[tierIndex][rarityIndex][1]));
                    }
                    else {
                        majorStats.put(RiftsStat.HP_REGEN, 50);
                    }
                }
                else {
                    majorStats.put(RiftsStat.ENERGY, 10);
                }
                if (tierIndex < 5 && rarityIndex < 4) {
                    majorStats.put(RiftsStat.HP, NumGenerator.rollInclusive(armorHP[tierIndex][rarityIndex][0], armorHP[tierIndex][rarityIndex][1]));
                }
                else {
                    //Hardcoding to 0 hp till fallen blesses the code with a better algorithm
                    majorStats.put(RiftsStat.HP, 0);
                }
                if (tierIndex < 5 && rarityIndex < 4) {
                    majorStats.put(RiftsStat.DEFENSE, NumGenerator.rollInclusive(armorDefense[tierIndex][rarityIndex][0], armorDefense[tierIndex][rarityIndex][1]));
                }
                else {
                    //Hardcoding to 0 hp till fallen blesses the code with a better algorithm
                    majorStats.put(RiftsStat.DEFENSE, 0);
                }
                break;
            case WEAPON:
                if (tierIndex < 5 && rarityIndex < 4) {
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

package me.Lozke.managers;

import me.Lozke.AgorianRifts;
import me.Lozke.data.*;
import me.Lozke.data.Scroll.Modifier;
import me.Lozke.data.Scroll.ScrollData;
import me.Lozke.data.Scroll.ScrollType;
import me.Lozke.utils.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemFactory {

    public static ItemStack newHelmet(Tier tier, Rarity rarity) {
        return createItem(tier, rarity, tier.getArmorMaterial(), "_HELMET");
    }
    public static ItemStack newChestplate(Tier tier, Rarity rarity) {
        return createItem(tier, rarity, tier.getArmorMaterial(), "_CHESTPLATE");
    }
    public static ItemStack newLeggings(Tier tier, Rarity rarity) {
        return createItem(tier, rarity, tier.getArmorMaterial(), "_LEGGINGS");
    }
    public static ItemStack newBoots(Tier tier, Rarity rarity) {
        return createItem(tier, rarity, tier.getArmorMaterial(), "_BOOTS");
    }

    //lol this would be the perfect place to return a Set<ItemStack>... just saying...
    public static ItemStack[] newSet(Tier tier, Rarity rarity) {
        return new ItemStack[]{newBoots(tier, rarity), newLeggings(tier, rarity), newChestplate(tier, rarity), newHelmet(tier, rarity)};
    }

    public static ItemStack newWeapon(Tier tier, Rarity rarity, String type) {
        return createItem(tier, rarity, tier.getWeaponMaterial(), "_" + type.toUpperCase());
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
        return new ItemWrapper()
                .setMaterial(Material.PAPER)
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

    private static ItemStack createItem(Tier tier, Rarity rarity, String material, String itemType) {
        ItemStack item = null;
        ItemWrapper itemWrapper = null;
        switch (itemType) {
            case "_HELMET":
            case "_CHESTPLATE":
            case "_LEGGINGS":
            case "_BOOTS":
                item = new ItemStack(Material.valueOf(material + itemType));
                itemWrapper = new ItemWrapper(item);

                if (NumGenerator.roll(2) == 1) {
                    itemWrapper.addKey(ARNamespacedKey.HP_REGEN, 50);
                }
                else {
                    itemWrapper.addKey(ARNamespacedKey.ENERGY_REGEN, 3);
                }

                int val = AgorianRifts.getGearData().getInt(tier.name() + "." + itemType.substring(1) + ".HI");
                double rarityMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + rarity.name()),
                        tierMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + tier.name());
                int loRange = (int) Math.ceil(Math.ceil(val * rarityMultiplier) - Math.ceil(val * tierMultiplier)), hiRange = (int) Math.ceil(val * rarityMultiplier);

                itemWrapper.addKey(ARNamespacedKey.HEALTH_POINTS, NumGenerator.rollInclusive(loRange, hiRange));
                break;
            case "_SWORD":
            case "_AXE":
            case "_SHOVEL":
            case "_HOE":
                item = new ItemStack(Material.valueOf(material + itemType));
                itemWrapper = new ItemWrapper(item);

                String valString = tier.name() + "." + itemType.substring(1);
                int lo = AgorianRifts.getGearData().getInt(valString + ".LO"), hi = AgorianRifts.getGearData().getInt(valString + ".HI");
                int mid = (lo+hi)/2;
                double tierMulti = AgorianRifts.getGearData().getDouble("MULTIPLIER.WEAPON." + tier.name()), rarityMulti = AgorianRifts.getGearData().getDouble("MULTIPLIER.WEAPON." + rarity.name());

                lo = (int) ((Math.ceil(Math.ceil(NumGenerator.rollInclusive(lo, mid) * rarityMulti) * tierMulti)) - Math.ceil(hi * 0.5));
                hi = (int) (Math.ceil(Math.ceil(NumGenerator.rollInclusive(mid, hi) * rarityMulti) * tierMulti));

                itemWrapper.addKey(ARNamespacedKey.DMG_LO, lo);
                itemWrapper.addKey(ARNamespacedKey.DMG_HI, hi);
                break;
        }
        if (item == null) {
            return null;
        }

        itemWrapper.addKey(ARNamespacedKey.REAL_ITEM);
        itemWrapper.addKey(ARNamespacedKey.TIER, tier.name());
        itemWrapper.addKey(ARNamespacedKey.RARITY, rarity.name());
        itemWrapper.addKey(ARNamespacedKey.CAN_ORB);
        itemWrapper.addKey(ARNamespacedKey.DURABILITY, tier.getMaxDurability());
        itemWrapper.addKey(ARNamespacedKey.MAX_DURABILITY, tier.getMaxDurability());
        itemWrapper.addKey(ARNamespacedKey.SCROLL_MAX_AMOUNT, NumGenerator.roll(10));

        itemWrapper.randomizeAttributes();

        item = itemWrapper.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        return item;
    }
}

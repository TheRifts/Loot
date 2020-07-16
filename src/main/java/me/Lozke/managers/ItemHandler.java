package me.Lozke.managers;

import me.Lozke.AgorianRifts;
import me.Lozke.data.*;
import me.Lozke.utils.Items;
import me.Lozke.utils.Logger;
import me.Lozke.utils.NumGenerator;
import me.Lozke.utils.Text;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ItemHandler {

    private static final int noWeaponEnergy = 5;
    private static final int weaponEnergy = 8;

    private static final int maxAttributes = 7;

    //ITEM CREATION
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
        ItemStack scrap = Scrap.types[tier.ordinal()].getItem();
        scrap.setAmount(amount);
        return scrap;
    }

    public static ItemStack newOrb(Tier tier) {
        return newOrb(tier, 1);
    }
    public static ItemStack newOrb(Tier tier, int amount) {
        ItemStack orb = Orb.types[tier.ordinal()].getItem();
        orb.setAmount(amount);
        return orb;
    }

    public static ItemStack newShard(Tier tier) {
        return newShard(tier, 1);
    }
    public static ItemStack newShard(Tier tier, int amount) {
        ItemStack shard = setTier(Shard.types[tier.ordinal()].getItem(), tier);
        shard.setAmount(amount);
        return shard;
    }

    public static ItemStack newGem() {
        return newGem(1);
    }
    public static ItemStack newGem(int amount) {
        ItemStack gem = Items.formatItem(Material.EMERALD, "&aAgorian Gem", new String[]{Text.colorize("&7The premier currency of the Agorian Rifts")});
        gem.setAmount(amount);
        ItemMeta itemMeta = gem.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(NamespacedKeys.realItem, PersistentDataType.STRING, "Certified AgorianRifts™ Item");
        gem.setItemMeta(itemMeta);
        return gem;
    }


    private static ItemStack createItem(Tier tier, Rarity rarity, String material, String itemType) {
        ItemStack item = null;
        ItemMeta itemMeta = null;
        PersistentDataContainer dataContainer = null;
        switch (itemType) {
            case "_HELMET":
            case "_CHESTPLATE":
            case "_LEGGINGS":
            case "_BOOTS":
                item = new ItemStack(Material.valueOf(material + itemType));
                addAttributes(item, getRandomAttributes(ItemType.ARMOR));
                itemMeta = item.getItemMeta();
                dataContainer = itemMeta.getPersistentDataContainer();
                if (NumGenerator.roll(2) == 1) {
                    dataContainer.set(NamespacedKeys.hpRegen, PersistentDataType.INTEGER, 50);
                }
                else {
                    dataContainer.set(NamespacedKeys.energyRegen, PersistentDataType.INTEGER, 3);
                }

                int val = AgorianRifts.getGearData().getInt(tier.name() + "." + itemType.substring(1) + ".HI");
                double rarityMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + rarity.name()),
                        tierMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + tier.name());
                int loRange = (int) Math.ceil(Math.ceil(val * rarityMultiplier) - Math.ceil(val * tierMultiplier)), hiRange = (int) Math.ceil(val * rarityMultiplier);
                Logger.broadcast(Math.ceil(Math.ceil(val * rarityMultiplier) - Math.ceil(val * tierMultiplier)) + "  " + Math.ceil(val * rarityMultiplier));
                Logger.broadcast(loRange + " / " + hiRange);

                dataContainer.set(NamespacedKeys.healthPoints, PersistentDataType.INTEGER, NumGenerator.rollInclusive(loRange, hiRange));
                break;
            case "_SWORD":
            case "_AXE":
            case "_SHOVEL":
            case "_HOE":
                item = new ItemStack(Material.valueOf(material + itemType));
                addAttributes(item, getRandomAttributes(ItemType.WEAPON));
                itemMeta = item.getItemMeta();
                dataContainer = itemMeta.getPersistentDataContainer();

                String valString = tier.name() + "." + itemType.substring(1);
                int lo = AgorianRifts.getGearData().getInt(valString + ".LO"), hi = AgorianRifts.getGearData().getInt(valString + ".HI");
                int mid = (lo+hi)/2;
                double tierMulti = AgorianRifts.getGearData().getDouble("MULTIPLIER.WEAPON." + tier.name()), rarityMulti = AgorianRifts.getGearData().getDouble("MULTIPLIER.WEAPON." + rarity.name());

                lo = (int) ((Math.ceil(Math.ceil(NumGenerator.rollInclusive(lo, mid) * rarityMulti) * tierMulti)) - Math.ceil(hi * 0.5));
                hi = (int) (Math.ceil(Math.ceil(NumGenerator.rollInclusive(mid, hi) * rarityMulti) * tierMulti));

                dataContainer.set(NamespacedKeys.dmg_lo, PersistentDataType.INTEGER, lo);
                dataContainer.set(NamespacedKeys.dmg_hi, PersistentDataType.INTEGER, hi);
                break;
        }
        dataContainer.set(NamespacedKeys.realItem, PersistentDataType.STRING, "Certified AgorianRifts™ Item");
        dataContainer.set(NamespacedKeys.tier, PersistentDataType.STRING, tier.name());
        dataContainer.set(NamespacedKeys.rarity, PersistentDataType.STRING, rarity.name());
        dataContainer.set(NamespacedKeys.canOrb, PersistentDataType.INTEGER, 1);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        return format(item);
    }

    //GENERAL ITEM UTILITIES
    public static void randomizeAttributes(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        Map map = dataContainer.get(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE);
        ItemType type = getItemType(itemStack);
        if(type == null) {
            Logger.log("Orb failed due to null item type");
            return;
        }
        dataContainer.remove(NamespacedKeys.attributes);
        addAttributes(itemStack, getRandomAttributes(type));
        format(itemStack);
    }
    //Calculations for odds of different #s of attributes
    /*
    assumes maxAttributes = 7

    First a uniform chance of 0 to 3.
    Next a repeated 65% chance to continue adding more up to maxAttributes.
    If 7 a 2/3 chance to be a uniform chance of 1 to maxAttributes-1.

    0:  0.25 * 0.35
     = 0.0875
    1:  0.25 * 0.35 +
        0.25 * 0.65 * 0.35
     = 0.144375 + 0.1047441205078125 * 1/3 * 1/6
     = 0.15601323561197916
    2:  0.25 * 0.35 +
        0.25 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.35
     = 0.18134375 + 0.1047441205078125 * 1/3 * 1/6
     = 0.19298198561197916
    3:  0.25 * 0.35 +
        0.25 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.35
     = 0.2053734375 + 0.1047441205078125 * 1/3 * 1/6
     = 0.21701167311197916
    4:  0.25 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.35
     = 0.133492734375 + 0.1047441205078125 * 1/3 * 1/6
     = 0.14513096998697916
    5:  0.25 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 * 0.35
     = 0.08677027734375 + 0.1047441205078125 * 1/3 * 1/6
     = 0.09840851295572916
    6:  0.25 * 0.65 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 * 0.35 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 * 0.35
     = 0.0564006802734375 + 0.1047441205078125 * 1/3 * 1/6
     = 0.068038915885416
    7:  0.25 * 0.65 * 0.65 * 0.65 * 0.65 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 +
        0.25 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65 * 0.65
     = 0.1047441205078125 * 1/3
     = 0.0349147068359375
     */
    //Values for odds of different #s of attributes
    /*
    0: 8.75%
    1: 15.60%
    2: 19.30%
    3: 21.70%
    4: 14.52%
    5: 9.84%
    6: 6.80%
    7: 3.49%
     */
    public static Attribute[] getRandomAttributes(ItemType itemType) {
        //First a uniform chance of 0 to 3.
        int amount = NumGenerator.rollInclusive(0, 3);
        //Next a repeated 65% chance to continue adding more up to maxAttributes.
        while (amount < maxAttributes && NumGenerator.roll(100) <= 65) {
            amount++;
        }
        //If maxAttributes a 2/3 chance to be a uniform chance of 1 to maxAttributes-1.
        if(amount == maxAttributes) {
            if(NumGenerator.roll(3) < 3) {
                amount = NumGenerator.roll(maxAttributes-1);
            }
        }

        ArrayList<Attribute> randomAttributes = new ArrayList<>();
        ArrayList<Attribute> attributes = new ArrayList<>();
        if (itemType.equals(ItemType.ARMOR)) {
            Collections.addAll(attributes, Attribute.armourValues);
        }
        if (itemType.equals(ItemType.WEAPON)) {
            Collections.addAll(attributes, Attribute.weaponValues);
        }
        while (randomAttributes.size() < amount && attributes.size() > 0) {
            int index = NumGenerator.index(attributes.size());
            Attribute attribute = attributes.get(index);
            attributes.remove(index);

            randomAttributes.add(attribute);
        }
        randomAttributes.toArray();
        return randomAttributes.toArray(new Attribute[randomAttributes.size()]);
    }

    public static void randomizeStats(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        Map map = dataContainer.get(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE);
        map.replaceAll((k, v) -> NumGenerator.rollInclusive(Attribute.valueOf(String.valueOf(k)).getMinValue(), Attribute.valueOf(String.valueOf(k)).getMaxValue()));
        dataContainer.set(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE, map);
        itemStack.setItemMeta(itemMeta);
        format(itemStack);
    }

    public static void addAttributes(ItemStack item, Attribute... attributes) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        HashMap<String, Integer> map = new HashMap<>();
        for (Attribute attribute : attributes) {
            map.put(attribute.name(), NumGenerator.rollInclusive(attribute.getMinValue(), attribute.getMaxValue()));
        }
        dataContainer.set(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE, map);
        item.setItemMeta(meta);
    }

    public static ItemStack format(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
        List<String> list = new ArrayList<>();
        if (dataContainer.has(NamespacedKeys.realItem, PersistentDataType.STRING)) {

            String[] itemType = item.getType().toString().split("_");

            if (dataContainer.has(NamespacedKeys.healthPoints, PersistentDataType.INTEGER)) {

                int val = AgorianRifts.getGearData().getInt(getTier(item).name() + "." + itemType[1] + ".HI");
                double rarityMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + getRarity(item).name()),
                        tierMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + getTier(item).name());
                int hiRange = (int) Math.ceil(val * rarityMultiplier);

                ChatColor statColor = percentageToColor((double)dataContainer.get(NamespacedKeys.healthPoints, PersistentDataType.INTEGER) / hiRange);
                list.add(Text.colorize("&7HP: " + statColor + "+" + dataContainer.get(NamespacedKeys.healthPoints, PersistentDataType.INTEGER)));
                if (dataContainer.has(NamespacedKeys.hpRegen, PersistentDataType.INTEGER)) {
                    list.add(Text.colorize("&7HP/s: &c+" + dataContainer.get(NamespacedKeys.hpRegen, PersistentDataType.INTEGER)));
                }
                if (dataContainer.has(NamespacedKeys.energyRegen, PersistentDataType.INTEGER)) {
                    list.add(Text.colorize("&7ENERGY: &c+" + dataContainer.get(NamespacedKeys.energyRegen, PersistentDataType.INTEGER) + "%"));
                }
            }
            if (dataContainer.has(NamespacedKeys.dmg_lo, PersistentDataType.INTEGER) && dataContainer.has(NamespacedKeys.dmg_hi, PersistentDataType.INTEGER)) {

                int loDMG = dataContainer.get(NamespacedKeys.dmg_lo, PersistentDataType.INTEGER);
                int hiDMG = dataContainer.get(NamespacedKeys.dmg_hi, PersistentDataType.INTEGER);
                int dmgHI = AgorianRifts.getGearData().getInt(getTier(item).name() + "." + itemType[1] + ".HI");

                ChatColor loStatColor = percentageToColor(loDMG/dmgHI);
                ChatColor hiStatColor = percentageToColor(hiDMG/dmgHI);

                list.add(Text.colorize("&7DMG: " + loStatColor + loDMG + "&7 - " + hiStatColor + hiDMG));
            }
            if (dataContainer.has(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE)) {
                Map valueMap = dataContainer.get(NamespacedKeys.attributes, NamespacedKeys.MAP_PERSISTENT_DATA_TYPE);
                Map percentageMap = new HashMap();
                for (Object key : valueMap.keySet()) {
                    percentageMap.put(key, (double)(int)valueMap.get(key) / Attribute.valueOf(String.valueOf(key)).getMaxValue());
                }
                percentageMap = sortByValue(percentageMap);

                StringBuilder sb = new StringBuilder();
                for (Object key : valueMap.keySet()) {
                    Attribute attribute = Attribute.valueOf(String.valueOf(key));
                    String loreDisplay = attribute.getLoreDisplayName();
                    String affix = attribute.getItemDisplayName();
                    int value = (int) valueMap.get(key);
                    ChatColor statColor = percentageToColor((double)percentageMap.get(key));
                    String[] split = loreDisplay.split(": ");
                    String lore = "&7" + split[0] + ": " + statColor + split[1].replace("{value}", String.valueOf(value));
                    list.add(Text.colorize(lore));
                    if (!affix.equalsIgnoreCase("")) {
                        sb.append(affix).append(" ");
                    }
                }

                String itemName = item.getType().toString().toLowerCase();
                if(itemName.contains("_")) {
                    itemName = itemName.substring(itemName.lastIndexOf("_"));
                    itemName = itemName.replace("_", " ");
                }
                else {
                    itemName = " " + itemName;
                }
                itemName = itemName.substring(0,2).toUpperCase() + itemName.substring(2);

                Tier tier = getTier(item);
                meta.setDisplayName(Text.colorize(tier.getColorCode() + sb.toString() + tier.getItemDisplayName() + itemName));
            }
            Rarity rarity = getRarity(item);
            list.add(Text.colorize(rarity.getColorCode() + "&o" + rarity.name().substring(0, 1) + rarity.name().substring(1).toLowerCase()));
        }
        meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }

    //Thanks stackoverflow!
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static net.md_5.bungee.api.ChatColor percentageToColor(double percentage) {
        if (percentage >= 0.25) {
            return getRollColor((percentage - 0.25) / 0.75, 0.166666666666667F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        return getRollColor(percentage / 0.25, 0.166666666666667F, 0.166666666666667F, 0.67F, 1.0F, 0.0F, 0.5F);
    }

    public static net.md_5.bungee.api.ChatColor getRollColor(double roll, float minHue, float maxHue, float minBright, float maxBright, float minSat, float maxSat) {
        roll = Math.pow(roll, 2.5f);
        float hue = minHue + (maxHue - minHue) * (float) roll;
        float saturation = minSat + (maxSat - minSat) * (float) roll;
        float brightness = minBright + (maxBright - minBright) * (float) roll;
        return net.md_5.bungee.api.ChatColor.of(Color.getHSBColor(hue, saturation, brightness));
    }

    public static boolean isRealItem(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            return item.getItemMeta().getPersistentDataContainer().has(NamespacedKeys.realItem, PersistentDataType.STRING);
        }
        return false;
    }

    public static boolean isTiered(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            return item.getItemMeta().getPersistentDataContainer().has(NamespacedKeys.tier, PersistentDataType.STRING);
        }
        return false;
    }
    public static Tier getTier(ItemStack item) {
        if (isTiered(item)) {
            return Tier.valueOf(item.getItemMeta().getPersistentDataContainer().get(NamespacedKeys.tier, PersistentDataType.STRING));
        }
        return null;
    }
    public static ItemStack setTier(ItemStack item, Tier tier) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(NamespacedKeys.tier, PersistentDataType.STRING, tier.name());
        item.setItemMeta(meta);
        return item;
    }

    public static Rarity getRarity(ItemStack item) {
        if (isRealItem(item)) {
            PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
            return Rarity.valueOf(dataContainer.get(NamespacedKeys.rarity, PersistentDataType.STRING));
        }
        return null;
    }
    public static ItemStack setRarity(ItemStack item, Rarity rarity) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
        dataContainer.set(NamespacedKeys.rarity, PersistentDataType.STRING, rarity.name());
        item.setItemMeta(meta);
        return item;
    }

    public static float getItemEnergyCost(ItemStack item) {
        if (isRealItem(item) && getItemType(item) == ItemType.WEAPON) {
            return weaponEnergy+getTier(item).getTierNumber();
        }
        return noWeaponEnergy;
    }

    public static int getDamage(ItemStack item) {
        if (isRealItem(item) && getItemType(item) == ItemType.WEAPON) {
            PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
            return NumGenerator.rollInclusive(dataContainer.get(NamespacedKeys.dmg_lo, PersistentDataType.INTEGER), dataContainer.get(NamespacedKeys.dmg_hi, PersistentDataType.INTEGER));
        }
        return 1;
    }

    public static ItemType getItemType(ItemStack item) {
        if (isRealItem(item)) {
            PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(NamespacedKeys.dmg_lo, PersistentDataType.INTEGER) && dataContainer.has(NamespacedKeys.dmg_hi, PersistentDataType.INTEGER)) {
                return ItemType.WEAPON;
            }
            else if (dataContainer.has(NamespacedKeys.healthPoints, PersistentDataType.INTEGER)) {
                return ItemType.ARMOR;
            }
        }
        return null;
    }
}

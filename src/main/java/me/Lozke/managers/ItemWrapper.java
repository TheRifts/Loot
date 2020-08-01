package me.Lozke.managers;

import me.Lozke.AgorianRifts;
import me.Lozke.data.*;
import me.Lozke.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ItemWrapper extends NamespacedKeyWrapper {
    private ItemStack item;
    private ItemMeta itemMeta;
    private boolean needsFormatting;

    private static final int noWeaponEnergy = 5;
    private static final int weaponEnergy = 8;
    private static final int maxAttributes = 7;

    public ItemWrapper(ItemStack item) {
        super(item);

        this.item = item;
        this.itemMeta = (item.getItemMeta() == null) ? Bukkit.getServer().getItemFactory().getItemMeta(item.getType()) : item.getItemMeta();
    }

    public ItemWrapper() {
        this(new ItemStack(Material.AIR));
    }

    public ItemStack getItem() {
        item.setItemMeta(itemMeta);
        if (needsFormatting) {
            return format();
        }
        return item;
    }

    public ItemWrapper setName(String name) {
        itemMeta.setDisplayName(Text.colorize(name));
        item.setItemMeta(itemMeta);
        return new ItemWrapper(item);
    }

    public ItemWrapper setLore(String... strings) {
        itemMeta.setLore(Arrays.asList(strings));
        item.setItemMeta(itemMeta);
        return new ItemWrapper(item);
    }

    public ItemWrapper setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemWrapper setMaterial(Material material) {
        item.setType(material);
        itemMeta = (item.getItemMeta() == null) ? Bukkit.getServer().getItemFactory().getItemMeta(item.getType()) : item.getItemMeta();
        return new ItemWrapper(item);
    }

    public ItemStack format() {
        this.needsFormatting = false;

        List<String> list = new ArrayList<>();
        if (hasKey(ARNamespacedKey.REAL_ITEM)) {
            String[] itemType = item.getType().toString().split("_");

            if (hasKey(ARNamespacedKey.HEALTH_POINTS)) {
                int val = AgorianRifts.getGearData().getInt(getTier().name() + "." + itemType[1] + ".HI");
                double rarityMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + getRarity().name()),
                        tierMultiplier = AgorianRifts.getGearData().getInt("MULTIPLIER.ARMOR." + getTier().name());
                int hiRange = (int) Math.ceil(val * rarityMultiplier);

                ChatColor statColor = percentageToColor((double) getInt(ARNamespacedKey.HEALTH_POINTS) / hiRange);
                list.add(Text.colorize("&7HP: " + statColor + "+" + get(ARNamespacedKey.HEALTH_POINTS)));

                if (hasKey(ARNamespacedKey.HP_REGEN)) {
                    list.add(Text.colorize("&7HP/s: &c+" + get(ARNamespacedKey.HP_REGEN)));
                }
                if (hasKey(ARNamespacedKey.ENERGY_REGEN)) {
                    list.add(Text.colorize("&7ENERGY: &c+" + get(ARNamespacedKey.ENERGY_REGEN) + "%"));
                }
            }

            if (hasKey(ARNamespacedKey.DMG_LO) && hasKey(ARNamespacedKey.DMG_HI)) {
                int loDMG = getInt(ARNamespacedKey.DMG_LO);
                int hiDMG = getInt(ARNamespacedKey.DMG_HI);
                int dmgHI = AgorianRifts.getGearData().getInt(getTier().name() + "." + itemType[1] + ".HI");

                ChatColor loStatColor = percentageToColor(loDMG/dmgHI);
                ChatColor hiStatColor = percentageToColor(hiDMG/dmgHI);

                list.add(Text.colorize("&7DMG: " + loStatColor + loDMG + "&7 - " + hiStatColor + hiDMG));
            }

            if (hasKey(ARNamespacedKey.ATTRIBUTES)) {
                Map valueMap = getMap(ARNamespacedKey.ATTRIBUTES);
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

                Tier tier = getTier();
                itemMeta.setDisplayName(Text.colorize(tier.getColorCode() + sb.toString() + tier.getItemDisplayName() + itemName));
            }
            Rarity rarity = getRarity();
            list.add(Text.colorize(rarity.getColorCode() + "&o" + rarity.name().substring(0, 1) + rarity.name().substring(1).toLowerCase()));
        }
        itemMeta.setLore(list);
        item.setItemMeta(itemMeta);
        return item;
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
    private Attribute[] getRandomAttributes(ItemType itemType) {
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

    public ItemWrapper randomizeAttributes() {
        ItemType type = getItemType();

        if(type == null) {
            Logger.log("Orb failed due to null item type");
            return this;
        }

        removeKey(ARNamespacedKey.ATTRIBUTES);

        addAttributes(getRandomAttributes(type));
        return this;
    }

    public ItemWrapper randomizeStats() {
        HashMap<String, Integer> map = (HashMap<String, Integer>) getMap(ARNamespacedKey.ATTRIBUTES);
        map.replaceAll((k, v) -> NumGenerator.rollInclusive(Attribute.valueOf(String.valueOf(k)).getMinValue(), Attribute.valueOf(String.valueOf(k)).getMaxValue()));
        addKey(ARNamespacedKey.ATTRIBUTES, map);

        this.needsFormatting = true;
        return this;
    }

    public ItemWrapper addAttributes(Attribute... attributes) {
        HashMap<String, Integer> map = new HashMap<>();
        for (Attribute attribute : attributes) {
            map.put(attribute.name(), NumGenerator.rollInclusive(attribute.getMinValue(), attribute.getMaxValue()));
        }
        addKey(ARNamespacedKey.ATTRIBUTES, map);
        this.needsFormatting = true;
        return this;
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

    public net.md_5.bungee.api.ChatColor percentageToColor(double percentage) {
        if (percentage >= 0.25) {
            return getRollColor((percentage - 0.25) / 0.75, 0.166666666666667F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        return getRollColor(percentage / 0.25, 0.166666666666667F, 0.166666666666667F, 0.67F, 1.0F, 0.0F, 0.5F);
    }

    public net.md_5.bungee.api.ChatColor getRollColor(double roll, float minHue, float maxHue, float minBright, float maxBright, float minSat, float maxSat) {
        roll = Math.pow(roll, 2.5f);
        float hue = minHue + (maxHue - minHue) * (float) roll;
        float saturation = minSat + (maxSat - minSat) * (float) roll;
        float brightness = minBright + (maxBright - minBright) * (float) roll;
        return net.md_5.bungee.api.ChatColor.of(Color.getHSBColor(hue, saturation, brightness));
    }

    public boolean isRealItem() {
        if (item == null || item.getItemMeta() == null) {
            return false;
        }
        return hasKey(ARNamespacedKey.REAL_ITEM);
    }

    public boolean isTiered() {
        if (item == null || item.getItemMeta() == null) {
            return false;
        }
        return hasKey(ARNamespacedKey.TIER);
    }

    public Tier getTier() {
        if (!isTiered()) {
            return null;
        }
        return Tier.valueOf(getString(ARNamespacedKey.TIER));
    }
    public ItemWrapper setTier(Tier tier) {
        addKey(ARNamespacedKey.TIER, tier.name());
        return this;
    }

    public Rarity getRarity() {
        if (!isRealItem()) {
            return null;
        }
        return Rarity.valueOf(getString(ARNamespacedKey.RARITY));
    }
    public ItemWrapper setRarity(Rarity rarity) {
        addKey(ARNamespacedKey.RARITY, rarity.name());
        return this;
    }

    public float getItemEnergyCost() {
        if (isRealItem() && getItemType() == ItemType.WEAPON) {
            return weaponEnergy+getTier().getTierNumber();
        }
        return noWeaponEnergy;
    }

    public int getDamage() {
        if (isRealItem() && getItemType() == ItemType.WEAPON) {
            return NumGenerator.rollInclusive(getInt(ARNamespacedKey.DMG_LO), getInt(ARNamespacedKey.DMG_HI));
        }
        return 1;
    }

    public ItemType getItemType() {
        if (isRealItem()) {
            if (hasKey(ARNamespacedKey.DMG_LO) && hasKey(ARNamespacedKey.DMG_HI)) {
                return ItemType.WEAPON;
            }
            else if (hasKey(ARNamespacedKey.HEALTH_POINTS) || hasKey(ARNamespacedKey.ENERGY_REGEN)) {
                return ItemType.ARMOR;
            }
        }
        return null;
    }

    /*
     * Overriding NamespacedKey Wrapper to return ItemWrapper to allow method chaining
     */

    @Override
    public ItemWrapper addKey(NamespacedKey namespacedKey, PersistentDataType dataType, Object key) {
        super.addKey(namespacedKey, dataType, key);
        this.itemMeta = item.getItemMeta();
        return this;
    }
    @Override
    public ItemWrapper addKey(ARNamespacedKey namespacedKey, Object key) {
        super.addKey(namespacedKey, key);
        this.itemMeta = item.getItemMeta();
        return this;
    }
    @Override
    public ItemWrapper addKey(ARNamespacedKey namespacedKey) {
        super.addKey(namespacedKey);
        this.itemMeta = item.getItemMeta();
        return this;
    }

    @Override
    public ItemWrapper removeKey(NamespacedKey namespacedKey) {
        super.removeKey(namespacedKey);
        this.itemMeta = item.getItemMeta();
        return this;
    }
    @Override
    public ItemWrapper removeKey(ARNamespacedKey namespacedKey) {
        super.removeKey(namespacedKey);
        this.itemMeta = item.getItemMeta();
        return this;
    }
}

package me.Lozke.managers;

import me.Lozke.data.*;
import me.Lozke.data.Scroll.Modifier;
import me.Lozke.data.Scroll.ScrollData;
import me.Lozke.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ItemWrapper extends NamespacedKeyWrapper {

    private ItemStack item;
    private boolean needsFormatting;

    private static final int noWeaponEnergy = 5;
    private static final int weaponEnergy = 8;
    private static final int maxAttributes = 7;

    @Nonnull
    public ItemWrapper(ItemStack item) {
        super(item);
        this.item = item;
    }

    @Nonnull
    public ItemWrapper(Material material) {
        this(new ItemStack(material));
    }

    public ItemStack getItem() {
        if (needsFormatting) {
            return format();
        }
        return item;
    }

    public ItemWrapper setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.colorize(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemWrapper setLore(String... strings) {
        item.setLore(Arrays.asList(strings));
        return this;
    }

    public ItemWrapper setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemWrapper setMaterial(Material material) {
        item.setType(material);
        return this;
    }

    public ItemWrapper setModelData(int val) {
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(val);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack format() {
        if (!hasKey(ARNamespacedKey.REAL_ITEM)) {
            return item;
        }

        needsFormatting = false;
        ItemMeta meta = item.getItemMeta();
        String multiplierFormat = "";

        List<String> lore = new ArrayList<>();
        String div = Text.colorize("&8&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯");
        Map<Modifier, Object> modifiers = getModifiers();

        lore.add(div);

        Tier tier = getTier();
        Rarity rarity = getRarity();
        lore.add(Text.colorize("&fRarity: " + rarity.getColorCode() + rarity.name().substring(0, 1) + rarity.name().substring(1).toLowerCase()));

        //Main Attribute Formatting
        Map majorStatsMap = getMap(ARNamespacedKey.MAJOR_STATS);
        ChatColor colorizedStat;
        switch (getItemType()) {
            case ARMOUR:
                int HEALTH_POINTS = (int) majorStatsMap.get(RiftsStat.HP);
                colorizedStat = colorizeStat(HEALTH_POINTS/(ItemFactory.getArmourHP(tier, rarity, ItemFactory.RangeType.HIGH) * 1.0));
                lore.add(Text.colorize("&fMax Health:" + colorizedStat + " +" + HEALTH_POINTS));

                int DEFENSE = (int) majorStatsMap.get(RiftsStat.DEFENSE);
                colorizedStat = colorizeStat(DEFENSE/(ItemFactory.getArmourDefense(tier, rarity, ItemFactory.RangeType.HIGH) * 1.0));
                lore.add(Text.colorize("&fDefense:" + colorizedStat + " +" + DEFENSE));

                if (majorStatsMap.containsKey(RiftsStat.HP_REGEN)) {
                    int HP_REGEN = (int) majorStatsMap.get(RiftsStat.HP_REGEN);
                    colorizedStat = colorizeStat(HP_REGEN/(ItemFactory.getArmourHPRegen(tier, rarity, ItemFactory.RangeType.HIGH) * 1.0));
                    lore.add(Text.colorize("&fHealth Regen:" + colorizedStat + " +" + HP_REGEN));
                }
                else if (majorStatsMap.containsKey(RiftsStat.ENERGY_REGEN)) {
                    int ENERGY_REGEN = (int) majorStatsMap.get(RiftsStat.ENERGY_REGEN);
                    colorizedStat = colorizeStat(ENERGY_REGEN/(10 * 1.0)); //HARDCODED VALUE AHHHHHHHH
                    lore.add(Text.colorize("&fEnergy Regen:" + colorizedStat + " +" + ENERGY_REGEN + "%"));
                }
                break;
            case WEAPON:
                double multiplier = modifiers.get(Modifier.DMG) == null ? 0 : (double) modifiers.get(Modifier.DMG);
                if (multiplier > 0) {
                    multiplierFormat = " &7(&b+"  + ((int)(multiplier * 100)) + "%&7)";
                }
                int dmgLow = (int) majorStatsMap.get(RiftsStat.DMG_LO);
                ChatColor colorizedLow = colorizeStat(dmgLow/(ItemFactory.getDamage(tier, rarity, ItemFactory.RangeType.HIGH) * 1.0));
                int dmgHigh = (int) majorStatsMap.get(RiftsStat.DMG_HI);
                ChatColor colorizedHigh = colorizeStat(dmgHigh/(ItemFactory.getDamage(tier, rarity, ItemFactory.RangeType.HIGH) * 1.0));
                lore.add(Text.colorize("&fAttack Damage: " + colorizedLow + dmgLow + "&f - " + colorizedHigh + dmgHigh + multiplierFormat));
                break;
        }
        lore.add(div);

        //Attributes Formatting
        Map valueMap = getMap(ARNamespacedKey.MINOR_STATS);
        Map percentageMap = new HashMap();
        for (Object key : valueMap.keySet()) {
            RiftsStat stat = RiftsStat.valueOf(String.valueOf(key));
            if (stat.getMin() == -1 && stat.getMax() == -1) continue;
            percentageMap.put(key, (double)(int)valueMap.get(key) / stat.getMax());
        }
        percentageMap = sortByValue(percentageMap);

        StringBuilder sb = new StringBuilder();
        for (Object key : valueMap.keySet()) {
            RiftsStat stat = RiftsStat.valueOf(String.valueOf(key));
            String loreDisplay = stat.getLoreDisplay();
            String affix = stat.getItemDisplay();
            int value = (int) valueMap.get(key);
            ChatColor statColor = colorizeStat((double)percentageMap.get(key));
            String[] split = loreDisplay.split(": ");
            String line = "";
            multiplierFormat = "";
            int modifier = modifiers.get(Modifier.ALL_STAT) == null ? 0 : (int) modifiers.get(Modifier.ALL_STAT);
            if (modifier > 0) {
                multiplierFormat = " &7(&b+" + modifier + "&7)";
            }
            line = "&f" + split[0] + ": " + statColor + split[1].replace("{value}", String.valueOf(value)) + multiplierFormat;
            lore.add(Text.colorize(line));
            if (!affix.equalsIgnoreCase("")) {
                sb.append(affix).append(" ");
            }
        }

        //Item Name Formatting
        String itemName = item.getType().toString().toLowerCase();
        if(itemName.contains("_")) {
            itemName = itemName.substring(itemName.lastIndexOf("_"));
            itemName = itemName.replace("_", " ");
        }
        else {
            itemName = " " + itemName;
        }
        itemName = itemName.substring(0,2).toUpperCase() + itemName.substring(2);
        meta.setDisplayName(Text.colorize(tier.getColorCode() + sb.toString() + tier.getItemDisplayName() + itemName));

        if (!valueMap.isEmpty()) {
            lore.add(div);
        }

        //Scroll Formatting
        char SCROLL = '۞';
        String OPEN_SLOT_COLOR = "&7";
        String USED_SLOT_COLOR = "&b";
        String FAILED_SLOT_COLOR = "&8";
        StringBuilder slotsLine = new StringBuilder();
        List<ScrollData> usedScrolls = (List<ScrollData>) getList(ARNamespacedKey.USED_SCROLLS);
        int totalSlots = getInt(ARNamespacedKey.SCROLL_MAX_AMOUNT);

        //Create scroll slot Line
        for (int i = 0; i < totalSlots; i++) {
            slotsLine.append(SCROLL);
        }

        //Gather slot usage data
        int openSlot = totalSlots;
        int usedSlot = 0;
        int failedSlot = 0;
        for (ScrollData scrollData : usedScrolls) {
            --openSlot;
            if (scrollData != null) usedSlot++;
            else failedSlot++;
        }
        //Colorize slot line based on usage data
        int colorOffset = 0;
        if (usedSlot > 0) {
            slotsLine.insert(0, USED_SLOT_COLOR);
            colorOffset += USED_SLOT_COLOR.length();
        }
        if (openSlot > 0) {
            slotsLine.insert(usedSlot + colorOffset, OPEN_SLOT_COLOR);
            colorOffset += OPEN_SLOT_COLOR.length();
        }
        if (failedSlot > 0) {
            slotsLine.insert(usedSlot + openSlot + colorOffset, FAILED_SLOT_COLOR);
        }

        slotsLine.insert(0, "&fScroll Slots: ");
        lore.add(Text.colorize(slotsLine.toString()));

        //Durability Formatting
        int durability = getInt(ARNamespacedKey.DURABILITY);
        int maxDurability = getInt(ARNamespacedKey.MAX_DURABILITY);
        double roll = durability / (maxDurability * 1.0);
        ChatColor color = getRollColor(roll, 0F, 0.222F, 1F, 1F, 1F, 1F);
        String bar = "▌▌▌▌▌▌▌▌▌▌";
        bar = " " + color + bar.substring(0, (int) (roll * 10)) + "&8" + bar.substring((int) (roll * 10)) + " ";
        lore.add(Text.colorize("&7Durability: &8" + durability + bar + maxDurability));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public Map<RiftsStat, Integer> getMajorStats() {
        return (Map<RiftsStat, Integer>) get(ARNamespacedKey.MAJOR_STATS);
    }

    public Map<RiftsStat, Integer> getMinorStats() {
        return (Map<RiftsStat, Integer>) get(ARNamespacedKey.MINOR_STATS);
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
    private RiftsStat[] getRandomStats(ItemType itemType) {
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

        ArrayList<RiftsStat> randomStats = new ArrayList<>();
        ArrayList<RiftsStat> stats = new ArrayList<>();
        if (itemType.equals(ItemType.ARMOUR)) {
            Collections.addAll(stats, RiftsStat.armourValues);
        }
        if (itemType.equals(ItemType.WEAPON)) {
            Collections.addAll(stats, RiftsStat.weaponValues);
        }
        while (randomStats.size() < amount && stats.size() > 0) {
            int index = NumGenerator.index(stats.size());
            RiftsStat attribute = stats.get(index);
            stats.remove(index);
            randomStats.add(attribute);
        }
        return randomStats.toArray(new RiftsStat[0]);
    }

    public ItemWrapper randomizeStats() {
        ItemType type = getItemType();

        if(type == null) {
            Logger.log("Randomizing minor stats failed due to null item type");
            return this;
        }

        removeKey(ARNamespacedKey.MINOR_STATS);
        addKey(ARNamespacedKey.MINOR_STATS);

        addStats(getRandomStats(type));
        return this;
    }

    /*
     * NOTE: ALL THESE STAT METHODS ARE ONLY FOR MINOR STATS.
     */

    public ItemWrapper randomizeStatValues() {
        HashMap<String, Integer> map = (HashMap<String, Integer>) getMap(ARNamespacedKey.MINOR_STATS);
        map.replaceAll((k, v) -> NumGenerator.rollInclusive(RiftsStat.valueOf(String.valueOf(k)).getMin(), RiftsStat.valueOf(String.valueOf(k)).getMax()));
        addKey(ARNamespacedKey.MINOR_STATS, map);

        this.needsFormatting = true;
        return this;
    }

    public ItemWrapper addStats(RiftsStat... stats) {
        HashMap<RiftsStat, Integer> statsMap = (HashMap<RiftsStat, Integer>) getMap(ARNamespacedKey.MINOR_STATS);
        for (RiftsStat stat : stats) {
            statsMap.put(stat, NumGenerator.rollInclusive(stat.getMin(), stat.getMax()));
        }
        addKey(ARNamespacedKey.MINOR_STATS, statsMap);
        this.needsFormatting = true;
        return this;
    }

    public ItemWrapper addStat(RiftsStat stat, Integer value) {
        HashMap<RiftsStat, Integer> statsMap = (HashMap<RiftsStat, Integer>) getMap(ARNamespacedKey.MINOR_STATS);
        statsMap.put(stat, value);
        addKey(ARNamespacedKey.MINOR_STATS, statsMap);
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

    public net.md_5.bungee.api.ChatColor colorizeStat(double percentage) {
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
            HashMap<RiftsStat, Integer> stats = (HashMap<RiftsStat, Integer>) getMap(ARNamespacedKey.MAJOR_STATS);
            return NumGenerator.rollInclusive(stats.get(RiftsStat.DMG_LO), stats.get(RiftsStat.DMG_LO));
        }
        return 1;
    }

    public ItemType getItemType() {
        if (isRealItem()) {
            HashMap<RiftsStat, Integer> stats = (HashMap<RiftsStat, Integer>) getMap(ARNamespacedKey.MAJOR_STATS);
            if (stats.containsKey(RiftsStat.HP)) return ItemType.ARMOUR;
            if (stats.containsKey(RiftsStat.DMG_LO) && stats.containsKey(RiftsStat.DMG_HI)) return ItemType.WEAPON;
        }
        return null;
    }

    public ItemWrapper addDurability(int durability) {
        int maxDurability = getInt(ARNamespacedKey.MAX_DURABILITY);
        int currentDurability = getInt(ARNamespacedKey.DURABILITY);
        addKey(ARNamespacedKey.DURABILITY, Math.min(maxDurability, currentDurability + durability));
        return updateDurability();
    }

    public ItemWrapper setDurability(int durability) {
        addKey(ARNamespacedKey.DURABILITY, durability);
        return updateDurability();
    }

    public int getDurabality() {
        return getInt(ARNamespacedKey.DURABILITY);
    }

    public ItemWrapper setMaxDurability(int durability) {
        addKey(ARNamespacedKey.MAX_DURABILITY, durability);
        return updateDurability();
    }

    public int getMaxDurability() {
        return getInt(ARNamespacedKey.MAX_DURABILITY);
    }

    public ItemWrapper setDurabilityAsPercentage(double durability) {
        int newDura = (int) (getInt(ARNamespacedKey.MAX_DURABILITY) * durability);
        newDura = Math.min(newDura, getInt(ARNamespacedKey.MAX_DURABILITY));
        addKey(ARNamespacedKey.DURABILITY, newDura);
        return updateDurability();
    }

    public double getDurabilityAsPercentage() {
        return (double) getDurabality() / getMaxDurability();
    }

    public ItemWrapper updateDurability() {
        List<String> lore = item.getLore();
        int durability = getInt(ARNamespacedKey.DURABILITY);
        int maxDurability = getInt(ARNamespacedKey.MAX_DURABILITY);
        double roll = durability / (maxDurability * 1.0);
        ChatColor color = getRollColor(roll, 0F, 0.222F, 1F, 1F, 1F, 1F);
        String bar = "▌▌▌▌▌▌▌▌▌▌";
        bar = " " + color + bar.substring(0, (int) (roll * 10)) + "&8" + bar.substring((int) (roll * 10)) + " ";
        lore.set(lore.size() - 1, Text.colorize("&7Durability: &8" + durability + bar + maxDurability));
        item.setLore(lore);

        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(item.getType().getMaxDurability() - (int)((item.getType().getMaxDurability() * getDurabilityAsPercentage())));
        item.setItemMeta((ItemMeta) meta);
        return this;
    }

    public Map<Modifier, Object> getModifiers() {
        Map<Modifier, Object> returnVal = new HashMap<>();
        for (ScrollData scroll : (List<ScrollData>) getList(ARNamespacedKey.USED_SCROLLS)) {
            if (scroll == null) continue;
            Map<Modifier, Object> modifiers = scroll.getScrollData();
            for (Modifier key : modifiers.keySet()) {
                if (returnVal.containsKey(key)) {
                    Object keyValObj = modifiers.get(key);
                    Object currentObj = returnVal.get(key);
                    if (keyValObj instanceof Integer && currentObj instanceof Integer) {
                        returnVal.put(key, ((int)currentObj + (int)keyValObj));
                    }
                    else if (keyValObj instanceof Double && currentObj instanceof Double) {
                        returnVal.put(key, ((double)currentObj + (double)keyValObj));
                    }
                }
                else {
                    returnVal.put(key, modifiers.get(key));
                }
            }
        }
        return returnVal;
    }

    /*
     * Overriding NamespacedKey Wrapper to return ItemWrapper to allow method chaining
     */

    @Override
    public ItemWrapper addKey(NamespacedKey namespacedKey, PersistentDataType dataType, Object key) {
        super.addKey(namespacedKey, dataType, key);
        return this;
    }
    @Override
    public ItemWrapper addKey(ARNamespacedKey namespacedKey, Object key) {
        super.addKey(namespacedKey, key);
        return this;
    }
    @Override
    public ItemWrapper addKey(ARNamespacedKey namespacedKey) {
        super.addKey(namespacedKey);
        return this;
    }

    @Override
    public ItemWrapper removeKey(NamespacedKey namespacedKey) {
        super.removeKey(namespacedKey);
        return this;
    }
    @Override
    public ItemWrapper removeKey(ARNamespacedKey namespacedKey) {
        super.removeKey(namespacedKey);
        return this;
    }
}

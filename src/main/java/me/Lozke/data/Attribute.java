package me.Lozke.data;

import java.util.ArrayList;

public enum Attribute {
    VIT(ItemType.ARMOR, "VIT: +{value}", "", 1, 1, 100 ),
    STR(ItemType.ARMOR, "STR: +{value}", "", 1, 1, 100),
    DEX(ItemType.ARMOR, "DEX: +{value}", "", 1, 1, 100),
    INT(ItemType.ARMOR, "INT: +{value}", "", 1, 1, 100),
    BLOCK(ItemType.ARMOR, "BLOCK: +{value}%", "Protective", 1, 1, 100),
    DODGE(ItemType.ARMOR, "DODGE: +{value}%", "Agile", 1, 1, 100),
    REFLECTION(ItemType.ARMOR, "REFLECTION: +{value}%", "Reflective", 1, 1, 100),
    THORNS(ItemType.ARMOR, "THORNS: +{value}%", "Spiked", 1, 1, 100),
    ELEMENTAL_RESISTANCE(ItemType.ARMOR, "ELEMENTAL RESISTANCE: +{value}%", "Elemental Resistant", 1, 1, 100),
    GEM_FIND(ItemType.ARMOR, "GEM FIND: +{value}%", "", 1, 1, 100),
    ITEM_FIND(ItemType.ARMOR, "ITEM FIND: +{value}%", "", 1, 1, 100),
    VS_PLAYER(ItemType.WEAPON, "vs. PLAYERS: +{value}%", "Slaying", 1, 1, 100),
    VS_MONSTER(ItemType.WEAPON, "vs. MONSTERS: +{value}%", "Slaughter", 1, 1, 100),
    FIRE(ItemType.WEAPON, "FIRE DMG: +{value}", "Fire", 1, 1, 100),
    ICE(ItemType.WEAPON, "ICE DMG: +{value}", "Ice", 1, 1, 100),
    POISON(ItemType.WEAPON, "POISON DMG: +{value}", "Poison", 1, 1, 100),
    PURE(ItemType.WEAPON, "PURE DMG: +{value}", "Pure", 1, 1, 100),
    CRIT_HIT(ItemType.WEAPON, "CRITICAL HIT: +{value}%", "Deadly", 1, 1, 100),
    BLUNT_HIT(ItemType.WEAPON, "BLUNT HIT: +{value}%", "Blunt", 1, 1, 100),
    ARMOR_PEN(ItemType.WEAPON, "ARMOR PENETRATION: +{value}%", "Penetrating", 1, 1, 100),
    SLOWNESS(ItemType.WEAPON, "SLOW: +{value}%", "Snaring", 1, 1, 100),
    ACCURACY(ItemType.WEAPON, "ACCURACY: +{value}%", "Accurate", 1, 1, 100),
    BLINDNESS(ItemType.WEAPON, "BLIND: +{value}%", "Blinding", 1, 1, 100),
    LIFE_STEAL(ItemType.WEAPON, "LIFE STEAL: +{value}%", "Vampyric", 1, 1, 100);


    public static final Attribute[] allValues = Attribute.values();
    public static final Attribute[] armourValues = getArmorValues();
    public static final Attribute[] weaponValues = getWeaponValues();

    private static Attribute[] getArmorValues() {
        ArrayList<Attribute> array = new ArrayList();
        for (Attribute attribute : allValues) {
            if (attribute.getItemType().equals(ItemType.ARMOR)) {
                array.add(attribute);
            }
        }
        return array.toArray(new Attribute[0]);
    }
    private static Attribute[] getWeaponValues() {
        ArrayList<Attribute> array = new ArrayList();
        for (Attribute attribute : allValues) {
            if (attribute.getItemType().equals(ItemType.WEAPON)) {
                array.add(attribute);
            }
        }
        return array.toArray(new Attribute[0]);
    }

    private final ItemType itemType;
    private final String loreDisplayName;
    private final String itemDisplayName;
    private final int displayWeight;
    private final int minValue;
    private final int maxValue;


    Attribute(ItemType itemType, String loreDisplayName, String itemDisplayName, int displayWeight, int minValue, int maxValue) {
        this.itemType = itemType;
        this.loreDisplayName = loreDisplayName;
        this.itemDisplayName = itemDisplayName;
        this.displayWeight = displayWeight;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }


    public ItemType getItemType() {
        return itemType;
    }
    public String getLoreDisplayName() {
        return loreDisplayName;
    }
    public String getItemDisplayName() {
        return itemDisplayName;
    }
    public int getDisplayWeight() {
        return displayWeight;
    }
    public int getMinValue() {
        return minValue;
    }
    public int getMaxValue() {
        return maxValue;
    }
}
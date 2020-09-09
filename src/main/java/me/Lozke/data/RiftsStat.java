package me.Lozke.data;

import java.util.ArrayList;

//TODO: Move all item display and min-max values to yml
public enum RiftsStat {
    HP(ItemType.ARMOR),
    HP_REGEN(ItemType.ARMOR),

    ENERGY(ItemType.ARMOR),
    ENERGY_REGEN(ItemType.ARMOR),

    DMG_LO(ItemType.WEAPON),
    DMG_HI(ItemType.WEAPON),

    DEFENSE(ItemType.ARMOR),

    GEM_FIND(ItemType.ARMOR, "Gem Find: +{value}%", "", 1, 100),
    ITEM_FIND(ItemType.ARMOR, "Item Find: +{value}%", "", 1, 100),

    VIT(ItemType.ARMOR, "VIT: +{value}", "", 1, 100),
    STR(ItemType.ARMOR, "STR: +{value}", "", 1, 100),
    DEX(ItemType.ARMOR, "DEX: +{value}", "", 1, 100),
    INT(ItemType.ARMOR, "INT: +{value}", "", 1, 100),

    PLAYER_MULTI(ItemType.WEAPON, "Player Damage: +{value}%", "Slaughtering", 1, 100),
    MONSTER_MULTI(ItemType.WEAPON, "Monster Damage: +{value}%", "Slaying", 1, 100),

    ACCURACY(ItemType.WEAPON, "Weapon Accuracy: +{value}%", "Accurate", 1, 100),

    BLOCK(ItemType.ARMOR, "Block Chance: +{value}%", "Protective", 1, 100),
    DODGE(ItemType.ARMOR, "Dodge Chance: +{value}%", "Agile", 1, 100),
    REFLECTION(ItemType.ARMOR, "Reflection Chance: +{value}%", "Reflective", 1, 100),
    THORNS(ItemType.ARMOR,  "Thorns Chance: +{value}%", "Spiked", 1, 100),

    ELEMENTAL_RESISTANCE(ItemType.ARMOR, "Elemental Resistance: +{value}%", "Elemental Resistant", 1, 100),

    FIRE_DAMAGE(ItemType.WEAPON, "Fire Damage: +{value}", "Fire", 1, 100),
    ICE_DAMAGE(ItemType.WEAPON, "Ice Damage: +{value}", "Icy", 1, 100),
    POISON_DAMAGE(ItemType.WEAPON, "Poison Damage: +{value}", "Poisonous", 1, 100),
    PURE_DAMAGE(ItemType.WEAPON, "Pure Damage: +{value}", "Pure", 1, 100),

    CRITICAL_HIT(ItemType.WEAPON, "Critical Chance: +{value}%", "Deadly", 1, 100),
    BLUNT_HIT(ItemType.WEAPON, "Blunt Hit Chance: +{value}%", "Blunt", 1, 100),
    ARMOUR_PEN(ItemType.WEAPON, "Armor Penetration Chance: +{value}%", "Penetrating", 1, 100),

    SLOWNESS(ItemType.WEAPON, "Slow Chance: +{value}%", "Snaring", 1, 100),
    BLINDNESS(ItemType.WEAPON, "Weapon Accuracy: +{value}%", "Accurate", 1, 100),

    LIFE_STEAL(ItemType.WEAPON, "Life Steal Chance: +{value}%", "Vampyric", 1, 100);

    public static final RiftsStat[] values = RiftsStat.values();
    public static final RiftsStat[] armourValues = getArmorValues();
    public static final RiftsStat[] weaponValues = getWeaponValues();

    private final ItemType itemType;
    private final String loreDisplay;
    private final String itemDisplay;
    private final int min;
    private final int max;

    RiftsStat(ItemType itemType, String loreDisplay, String itemDisplay, int min, int max) {
        this.itemType = itemType;
        this.loreDisplay = loreDisplay;
        this.itemDisplay = itemDisplay;
        this.min = min;
        this.max = max;
    }
    RiftsStat(ItemType itemType) {
        this(itemType, "", "", -1, -1);
    }

    public ItemType getItemType() {
        return itemType;
    }
    public String getLoreDisplay() {
        return loreDisplay;
    }
    public String getItemDisplay() {
        return itemDisplay;
    }
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }

    private static RiftsStat[] getArmorValues() {
        ArrayList<RiftsStat> array = new ArrayList<>();
        for (RiftsStat stat : values) {
            if (stat.getItemType().equals(ItemType.ARMOR) && !(stat.getMin() == -1 && stat.getMax() == -1)) {
                array.add(stat);
            }
        }
        return array.toArray(new RiftsStat[0]);
    }
    private static RiftsStat[] getWeaponValues() {
        ArrayList<RiftsStat> array = new ArrayList<>();
        for (RiftsStat stat : values) {
            if (stat.getItemType().equals(ItemType.WEAPON) && !(stat.getMin() == -1 && stat.getMax() == -1)) {
                array.add(stat);
            }
        }
        return array.toArray(new RiftsStat[0]);
    }
}

package me.Lozke.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

//TODO: Move all item display and min-max values to yml
@AllArgsConstructor
public enum RiftsStat {
    HP(ItemType.ARMOUR),
    HP_REGEN(ItemType.ARMOUR),

    ENERGY(ItemType.ARMOUR),
    ENERGY_REGEN(ItemType.ARMOUR),

    DMG_LO(ItemType.WEAPON),
    DMG_HI(ItemType.WEAPON),

    DEFENSE(ItemType.ARMOUR),

    GEM_FIND(ItemType.ARMOUR, "Gem Find: +{value}%", "", 1, 100),
    ITEM_FIND(ItemType.ARMOUR, "Item Find: +{value}%", "", 1, 100),

    VIT(ItemType.ARMOUR, "VIT: +{value}", "", 1, 100),
    STR(ItemType.ARMOUR, "STR: +{value}", "", 1, 100),
    DEX(ItemType.ARMOUR, "DEX: +{value}", "", 1, 100),
    INT(ItemType.ARMOUR, "INT: +{value}", "", 1, 100),

    PLAYER_MULTI(ItemType.WEAPON, "Player Damage: +{value}%", "Slaughtering", 1, 100),
    MONSTER_MULTI(ItemType.WEAPON, "Monster Damage: +{value}%", "Slaying", 1, 100),

    ACCURACY(ItemType.WEAPON, "Weapon Accuracy: +{value}%", "Accurate", 1, 100),

    BLOCK(ItemType.ARMOUR, "Block Chance: +{value}%", "Protective", 1, 100),
    DODGE(ItemType.ARMOUR, "Dodge Chance: +{value}%", "Agile", 1, 100),
    REFLECTION(ItemType.ARMOUR, "Reflection Chance: +{value}%", "Reflective", 1, 100),
    THORNS(ItemType.ARMOUR,  "Thorns Chance: +{value}%", "Spiked", 1, 100),

    ELEMENTAL_RESISTANCE(ItemType.ARMOUR, "Elemental Resistance: +{value}%", "Elemental Resistant", 1, 100),

    FIRE_DAMAGE(ItemType.WEAPON, "Fire Damage: +{value}", "Fire", 1, 100),
    ICE_DAMAGE(ItemType.WEAPON, "Ice Damage: +{value}", "Icy", 1, 100),
    POISON_DAMAGE(ItemType.WEAPON, "Poison Damage: +{value}", "Poisonous", 1, 100),
    PURE_DAMAGE(ItemType.WEAPON, "Pure Damage: +{value}", "Pure", 1, 100),

    CRITICAL_HIT(ItemType.WEAPON, "Critical Chance: +{value}%", "Deadly", 1, 100),
    BLUNT_HIT(ItemType.WEAPON, "Blunt Hit Chance: +{value}%", "Blunt", 1, 100),
    ARMOUR_PEN(ItemType.WEAPON, "Armour Penetration Chance: +{value}%", "Penetrating", 1, 100),

    SLOWNESS(ItemType.WEAPON, "Slow Chance: +{value}%", "Snaring", 1, 100),
    BLINDNESS(ItemType.WEAPON, "Weapon Accuracy: +{value}%", "Accurate", 1, 100),

    LIFE_STEAL(ItemType.WEAPON, "Life Steal Chance: +{value}%", "Vampyric", 1, 100);

    public static final RiftsStat[] values = RiftsStat.values();
    public static final RiftsStat[] armourValues = getArmourValues();
    public static final RiftsStat[] weaponValues = getWeaponValues();

    @Getter private final ItemType itemType;
    @Getter private final String loreDisplay;
    @Getter private final String itemDisplay;
    @Getter private final int min;
    @Getter private final int max;

    RiftsStat(ItemType itemType) {
        this(itemType, "", "", -1, -1);
    }

    private static RiftsStat[] getArmourValues() {
        ArrayList<RiftsStat> array = new ArrayList<>();
        for (RiftsStat stat : values) {
            if (stat.getItemType().equals(ItemType.ARMOUR) && !(stat.getMin() == -1 && stat.getMax() == -1)) {
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

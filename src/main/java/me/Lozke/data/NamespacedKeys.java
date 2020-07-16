package me.Lozke.data;

import me.Lozke.AgorianRifts;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class NamespacedKeys {

    final private static AgorianRifts plugin = AgorianRifts.getPluginInstance();

    final public static NamespacedKey spawnerWandToggle = new NamespacedKey(plugin, "spawner-wand-toggle"); //Let's convert this to a boolean data type!

    final public static NamespacedKey realItem = new NamespacedKey(plugin, "a-real-item");
    final public static NamespacedKey tier = new NamespacedKey(plugin, "tier");
    final public static NamespacedKey rarity = new NamespacedKey(plugin, "rarity");
    final public static NamespacedKey healthPoints = new NamespacedKey(plugin, "health-points");
    final public static NamespacedKey dmg_lo = new NamespacedKey(plugin, "dmg-lo");
    final public static NamespacedKey dmg_hi = new NamespacedKey(plugin, "dmg-hi");
    final public static NamespacedKey hpRegen = new NamespacedKey(plugin, "hp-regen");
    final public static NamespacedKey energyRegen = new NamespacedKey(plugin, "energy-regen");

    final public static NamespacedKey canOrb = new NamespacedKey(plugin, "can-orb"); //Let's convert this to a boolean data type!

    final public static NamespacedKey attributes = new NamespacedKey(plugin, "item-attributes");

    final public static PersistentDataType<byte[], Map> MAP_PERSISTENT_DATA_TYPE = new MapDataType();
}

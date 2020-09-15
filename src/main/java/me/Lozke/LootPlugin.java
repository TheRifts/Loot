package me.Lozke;

import co.aikar.commands.BukkitCommandManager;
import me.Lozke.commands.*;
import me.Lozke.data.*;
import me.Lozke.listeners.*;
import me.Lozke.managers.AnvilManager;
import me.Lozke.managers.EquipmentManager;
import me.Lozke.managers.ItemFactory;
import me.Lozke.managers.StatManager;
import me.Lozke.utils.Logger;
import me.Lozke.utils.config.SmartYamlConfiguration;
import me.Lozke.utils.config.VersionedConfiguration;
import me.Lozke.utils.config.VersionedSmartYamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LootPlugin extends JavaPlugin {

    private static LootPlugin plugin;
    private BukkitCommandManager commandManager;
    private ItemFactory itemFactory;
    private AnvilManager anvilManager;
    private EquipmentManager equipmentManager;
    private StatManager statManager;

    private static SmartYamlConfiguration gearData;

    @Override
    public void onEnable() {
        plugin = this;
        commandManager = new BukkitCommandManager(this);
        registerCommandCompletion("tier", Stream.of(Tier.types).map(Enum::name).collect(Collectors.toList()));
        registerCommandCompletion("rarity", Stream.of(Rarity.types).map(Enum::name).collect(Collectors.toList()));
        registerCommandCompletion("item-type", Stream.of(ItemType.types).map(Enum::name).collect(Collectors.toList()));
        registerCommandCompletion("armour-type", Stream.of(ArmourType.types).map(Enum::name).collect(Collectors.toList()));
        registerCommandCompletion("weapon-type", Stream.of(WeaponType.types).map(Enum::name).collect(Collectors.toList()));

        gearData = defaultSettingsLoad("geardata.yml");

        this.itemFactory = new ItemFactory();
        this.anvilManager = new AnvilManager();
        this.equipmentManager = new EquipmentManager();
        this.statManager = new StatManager();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ModifyingItemByClickListener(), this);
        pm.registerEvents(new PouchListener(), this);
        pm.registerEvents(new AnvilListener(), this);
        pm.registerEvents(new ItemDurabilityDamageListener(), this);
        pm.registerEvents(new AnvilChatListener(), this);
        pm.registerEvents(new ScrollRightClickListener(), this);

        commandManager.registerCommand(new CheckCommand());
        commandManager.registerCommand(new CreateItem());
        commandManager.registerCommand(new ItemRename());
        commandManager.registerCommand(new Reload());
        commandManager.registerCommand(new SetDurabalityPercent());
        commandManager.registerCommand(new ValueCommand());

        Logger.log(this, "The monkeys are cranking out loot (\u001b[32mPlugin Enabled\u001b[0m)");
    }

    @Override
    public void onDisable() {
        Logger.log(this, "The monkeys have left given up on making loot (\u001b[31mPlugin Disabled\u001b[0m)");
    }

    private VersionedSmartYamlConfiguration defaultSettingsLoad(String name) {
        return new VersionedSmartYamlConfiguration(new File(getDataFolder(), name),
                getResource(name), VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    }

    public void registerCommandCompletion(String id, Collection<String> completions) {
        commandManager.getCommandCompletions().registerAsyncCompletion(id, c -> completions);
    }

    public static LootPlugin getPluginInstance() {
        return plugin;
    }

    public static FileConfiguration getGearData() {
        return gearData;
    }

    public ItemFactory getItemFactoryInstance() {
        return itemFactory;
    }

    public AnvilManager getAnvilManager() {
        return anvilManager;
    }

    public EquipmentManager getEquipmentManager() {
        return equipmentManager;
    }

    public StatManager getStatManager() {
        return statManager;
    }
}

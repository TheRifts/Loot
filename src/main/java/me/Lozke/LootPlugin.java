package me.Lozke;

import co.aikar.commands.BukkitCommandManager;
import me.Lozke.commands.*;
import me.Lozke.data.*;
import me.Lozke.listeners.*;
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

}

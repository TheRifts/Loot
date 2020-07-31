package me.Lozke;

import me.Lozke.commands.*;
import me.Lozke.listeners.ModifyingItemByClickListener;
import me.Lozke.listeners.PouchListener;
import me.Lozke.utils.config.SmartYamlConfiguration;
import me.Lozke.utils.config.VersionedConfiguration;
import me.Lozke.utils.config.VersionedSmartYamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;

public class LootPlugin extends JavaPlugin {

    private static LootPlugin plugin;

    private static SmartYamlConfiguration gearData;

    @Override
    public void onEnable() {

        gearData = defaultSettingsLoad("geardata.yml");

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ModifyingItemByClickListener(), this);
        pm.registerEvents(new PouchListener(), this);

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(this.getName(), new CheckCommand());
            commandMap.register(this.getName(), new CreateItem());
            commandMap.register(this.getName(), new ItemRename());
            commandMap.register(this.getName(), new ValueCommand());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    private VersionedSmartYamlConfiguration defaultSettingsLoad(String name) {
        return new VersionedSmartYamlConfiguration(new File(getDataFolder(), name),
                getResource(name), VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    }

    public static LootPlugin getPluginInstance() {
        return plugin;
    }

    public static FileConfiguration getGearData() {
        return gearData;
    }

}

package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.LootPlugin;
import org.bukkit.entity.Player;

@CommandAlias("reload")
public class Reload extends BaseCommand {
    @Default
    public static void onReload(Player player) {
        LootPlugin.getPluginInstance().onDisable();
        LootPlugin.getPluginInstance().onEnable();
    }
}

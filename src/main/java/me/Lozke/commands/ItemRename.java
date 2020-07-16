package me.Lozke.commands;

import me.Lozke.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemRename extends Command {

    public ItemRename() {
        super("rename");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = handItem.getItemMeta();
        itemMeta.setDisplayName(Text.colorize(String.join(" ", args)));
        handItem.setItemMeta(itemMeta);
        return true;
    }
}

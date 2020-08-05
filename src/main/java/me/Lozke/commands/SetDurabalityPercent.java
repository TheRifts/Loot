package me.Lozke.commands;

import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDurabalityPercent extends Command {

    public SetDurabalityPercent() {
        super("setdurabalitypercent");
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        Player player = (Player) commandSender;
        if (player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(Text.colorize("Hold an item"));
            return true;
        }
        ItemWrapper itemWrapper = new ItemWrapper(player.getItemInHand());
        if (!itemWrapper.isRealItem()) {
            return true;
        }
        double percent = Double.parseDouble(args[0]);
        if (percent < 0) {
            player.sendMessage("cannot be negative.");
            return true;
        }
        if (percent > 0.99) {
            percent = percent / 100;
        }
        itemWrapper.setDurabilityAsPercentage(percent);
        return true;
    }
}

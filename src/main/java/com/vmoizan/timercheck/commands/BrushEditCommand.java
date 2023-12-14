package com.vmoizan.timercheck.commands;

import com.vmoizan.timercheck.TimerCheck;
import com.vmoizan.timercheck.guis.BrushSelectorGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /brush command class
 */
public class BrushEditCommand implements CommandExecutor {

    /**
     * Function called when a player use the /brush command
     * @param sender
     * @param command
     * @param s
     * @param strings
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            BrushSelectorGui gui = new BrushSelectorGui(p);
            Bukkit.getPluginManager().registerEvents(gui , TimerCheck.getInstance());
            gui.openInventory();
            return true;
        }
        return false;
    }
}

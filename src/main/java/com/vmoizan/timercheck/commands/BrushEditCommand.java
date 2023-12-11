package com.vmoizan.timercheck.commands;

import com.vmoizan.timercheck.TimerCheck;
import com.vmoizan.timercheck.guis.BrushSelectorGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BrushEditCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            BrushSelectorGui gui = new BrushSelectorGui();
            Bukkit.getPluginManager().registerEvents(gui , TimerCheck.getInstance());
            gui.openInventory(p);
            return true;
        }
        return false;
    }
}

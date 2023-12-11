package com.vmoizan.timercheck;

import com.vmoizan.timercheck.commands.BrushEditCommand;
import com.vmoizan.timercheck.data.DataManager;
import com.vmoizan.timercheck.events.JoinQuitEvents;
import com.vmoizan.timercheck.guis.SignMenuFactory;
import com.vmoizan.timercheck.listeners.ChatListener;
import com.vmoizan.timercheck.listeners.HeldItemListener;
import com.vmoizan.timercheck.listeners.PacketListener;
import com.vmoizan.timercheck.listeners.PlayerMovementListener;
import com.vmoizan.timercheck.task.PaintTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TimerCheck extends JavaPlugin {

    private static TimerCheck instance;
    private DataManager dataManager;
    private SignMenuFactory signMenuFactory;
    @Override
    public void onEnable() {
        instance = this;
        this.dataManager = new DataManager();
        // Plugin startup logic

        initListeners();

        initCommands();

        this.signMenuFactory = new SignMenuFactory(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void initListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerMovementListener(), this);
        Bukkit.getPluginManager().registerEvents(new HeldItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        PacketListener packetListener = new PacketListener(this);
    }

    public void initCommands(){
        this.getCommand("brush").setExecutor(new BrushEditCommand());
    }

    public SignMenuFactory getSignMenuFactory() {
        return this.signMenuFactory;
    }

    public static TimerCheck getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}

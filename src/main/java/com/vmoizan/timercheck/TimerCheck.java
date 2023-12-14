package com.vmoizan.timercheck;

import com.mongodb.client.*;
import com.vmoizan.timercheck.commands.BrushEditCommand;
import com.vmoizan.timercheck.data.DataManager;
import com.vmoizan.timercheck.events.JoinQuitEvents;
import com.vmoizan.timercheck.guis.SignMenuFactory;
import com.vmoizan.timercheck.listeners.ChatListener;
import com.vmoizan.timercheck.listeners.PacketListener;
import org.bson.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static com.mongodb.client.model.Filters.eq;

public final class TimerCheck extends JavaPlugin {

    private static TimerCheck instance;
    private DataManager dataManager;
    private SignMenuFactory signMenuFactory;
    private MongoClient mongoClient;
    @Override
    public void onEnable() {
        instance = this;

        String uri = "mongodb+srv://valmoizan:ocL5ZDzhDJyMFOqz@cluster0.jacmh3e.mongodb.net/?retryWrites=true&w=majority";
        try {
            mongoClient = MongoClients.create(uri);
        }catch(Exception e){
            System.out.println("Error connecting to MONGODB");
        }

        this.dataManager = new DataManager();
        // Plugin startup logic

        initListeners();

        initCommands();

        this.signMenuFactory = new SignMenuFactory(this);
    }

    @Override
    public void onDisable() {
        System.out.println("CLOSED CONNECTION");
        TimerCheck.getInstance().mongoClient.close();
    }

    public void initListeners(){
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

    public MongoDatabase getDatabase() {
        return mongoClient.getDatabase("brush_plugin");
    }

    public MongoCollection<Document> getPlayerDataCollection() {
        return getDatabase().getCollection("player_data");
    }
}

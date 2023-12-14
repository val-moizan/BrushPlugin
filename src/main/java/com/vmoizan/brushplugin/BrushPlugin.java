package com.vmoizan.brushplugin;

import com.mongodb.client.*;
import com.mongodb.lang.*;
import com.vmoizan.brushplugin.commands.*;
import com.vmoizan.brushplugin.data.*;
import com.vmoizan.brushplugin.events.*;
import com.vmoizan.brushplugin.guis.*;
import com.vmoizan.brushplugin.listeners.*;
import org.bson.*;
import org.bson.conversions.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Brush plugin main class
 */
public final class BrushPlugin extends JavaPlugin {

    /**
     * Plugin instance
     */
    private static BrushPlugin instance;

    /**
     * Plugin data manager
     */
    private DataManager dataManager;

    /**
     * Plugin sign menu factory
     */
    private SignMenuFactory signMenuFactory;

    /**
     * Plugin access to database
     * Can be null if can't connect
     */
    private MongoClient mongoClient;

    /**
     * Triggered when plugin is enabled
     */
    @Override
    public void onEnable() {
        instance = this;

        initConfig();
        initDatabase();

        this.dataManager = new DataManager();

        initListeners();

        initCommands();

        this.signMenuFactory = new SignMenuFactory(this);
    }

    @Override
    public void onDisable() {

    }

    /**
     * Init config
     */
    private void initConfig(){
        this.saveDefaultConfig();
        String url = (String) this.getConfig().get("mongoDbUrl");
        if (url == null) {
            this.getConfig().set("mongoDbUrl", "<your_db_url>");
            this.saveConfig();
        }
    }

    /**
     * Init link to data base
     */
    private void initDatabase(){
        String url = (String)this.getConfig().get("mongoDbUrl");
        if(url != null){
            try {
                mongoClient = MongoClients.create(url);
                Document ping = new Document();
                ping.append("ping", "1");
                //check if connection is valid
                mongoClient.getDatabase("brush_plugin").runCommand(ping);
            }catch(Exception e){
                mongoClient = null;
                Bukkit.getLogger().log(Level.SEVERE, "Error connecting to database");
            }
        }

    }

    /**
     * Init all listeners
     */
    private void initListeners(){
        Bukkit.getPluginManager().registerEvents(new JoinQuitEvents(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        PacketListener packetListener = new PacketListener(this);
    }

    /**
     * Init all commands
     */
    private void initCommands(){
        this.getCommand("brush").setExecutor(new BrushEditCommand());
    }

    /**
     * Get sign menu factory
     * @return sign menu factory
     */
    public SignMenuFactory getSignMenuFactory() {
        return this.signMenuFactory;
    }

    /**
     * Get plugin instance
     * @return instance
     */
    public static BrushPlugin getInstance() {
        return instance;
    }

    /**
     * Get plugin data manager
     * @return datamanager
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Get plugin database
     * @return mongo database
     */
    @Nullable
    public MongoDatabase getDatabase() {
        return mongoClient == null ? null : mongoClient.getDatabase("brush_plugin");
    }

    /**
     * Get player data collection from database
     * @return mongo collection
     */
    @Nullable
    public MongoCollection<Document> getPlayerDataCollection() {
        MongoDatabase db = getDatabase();
        return db == null ? null : db.getCollection("player_data");
    }
}

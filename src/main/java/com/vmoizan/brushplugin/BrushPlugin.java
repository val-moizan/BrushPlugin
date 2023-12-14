package com.vmoizan.brushplugin;

import com.mongodb.client.*;
import com.vmoizan.brushplugin.commands.*;
import com.vmoizan.brushplugin.data.*;
import com.vmoizan.brushplugin.events.*;
import com.vmoizan.brushplugin.guis.*;
import com.vmoizan.brushplugin.listeners.*;
import org.bson.*;
import org.bukkit.*;
import org.bukkit.plugin.java.*;

import java.io.*;
import java.util.*;

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
     */
    private MongoClient mongoClient;

    /**
     * Triggered when plugin is enabled
     */
    @Override
    public void onEnable() {
        instance = this;
        String uri;
        try (InputStream input = BrushPlugin.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            uri = prop.getProperty("mongoDbUrl");
        } catch (IOException ex) {
            ex.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            mongoClient = MongoClients.create(uri);
        }catch(Exception e){
            System.out.println("Error connecting to MONGODB");
        }

        this.dataManager = new DataManager();

        initListeners();

        initCommands();

        this.signMenuFactory = new SignMenuFactory(this);
    }

    @Override
    public void onDisable() {

    }

    /**
     * Init all listeners
     */
    public void initListeners(){
        Bukkit.getPluginManager().registerEvents(new JoinQuitEvents(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        PacketListener packetListener = new PacketListener(this);
    }

    /**
     * Init all commands
     */
    public void initCommands(){
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
    public MongoDatabase getDatabase() {
        return mongoClient.getDatabase("brush_plugin");
    }

    /**
     * Get player data collection from database
     * @return mongo collection
     */
    public MongoCollection<Document> getPlayerDataCollection() {
        return getDatabase().getCollection("player_data");
    }
}

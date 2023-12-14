package com.vmoizan.brushplugin.data;


import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.result.*;
import com.vmoizan.brushplugin.*;
import org.bson.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

/**
 * Online players data management class
 */
public class DataManager {

    /**
     * Set of online players data
     */
    private Set<PlayerData> dataSet = new HashSet<>();

    /**
     * Default constructor
     * Automatically add players data (in case of a reload)
     */
    public DataManager() {
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    /**
     * Add a new player to the manager
     * @param player: the player
     */
    public void addPlayer(Player player) {
        PlayerData playerData = null;
        try{
            MongoCollection<Document> collection = BrushPlugin.getInstance().getDatabase().getCollection("player_data");
            Document doc = collection.find(eq("uuid", player.getUniqueId().toString())).first();

            if (doc != null) {
                System.out.println("Getting player data");
                playerData = new PlayerData(player, doc);
            } else {
                System.out.println("New player detected, adding to database");
                playerData = new PlayerData(player);
                InsertOneResult result = collection.insertOne(playerData.toDocument());
            }
        }catch(MongoException ex){
            System.err.println("Unable to load player '" + player.getName() + "' data");
            player.kickPlayer("Unable to load your data. Please reconnect");
            return;
        }

        this.add(playerData);
    }

    /**
     * Get a data from a specific player
     * @param player
     * @return PlayerData
     */
    public PlayerData getDataPlayer(Player player) {
        return dataSet.stream().filter(playerData -> playerData.getPlayer() == player).findFirst().orElse(null);
    }

    /**
     * Add a new player data to the manager
     * @param playerData: the data
     */
    private void add(PlayerData playerData) {
        dataSet.add(playerData);
    }

    /**
     * Remove the data from a player
     * @param player
     */
    public void remove(Player player) {
        dataSet.removeIf(playerData -> playerData.getPlayer() == player);
    }

    /**
     * Return the set of data
     * @return
     */
    public Set<PlayerData> getDataSet() {
        return dataSet;
    }

}
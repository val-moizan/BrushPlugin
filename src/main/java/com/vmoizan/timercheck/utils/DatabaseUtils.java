package com.vmoizan.timercheck.utils;

import com.mongodb.client.*;
import com.vmoizan.timercheck.*;
import com.vmoizan.timercheck.data.*;
import org.bson.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class DatabaseUtils {

    /**
     * Update the player's brush material in database
     * @param p: player
     * @param material: material
     */
    public static void updateBrushMaterial(Player p, Material material){
        MongoCollection<Document> collection = TimerCheck.getInstance().getPlayerDataCollection();
        Document query = new Document();
        query.append("uuid", p.getUniqueId().toString());
        Document setData = new Document();
        setData.append("brush_material", material);
        Document update = new Document();
        update.append("$set", setData);
        collection.updateOne(query, update);
    }

    /**
     * Update the player's brush range in database
     * @param p: player
     * @param range: range
     */
    public static void updateBrushRange(Player p, int range){
        MongoCollection<Document> collection = TimerCheck.getInstance().getPlayerDataCollection();
        Document query = new Document();
        query.append("uuid", p.getUniqueId().toString());
        Document setData = new Document();
        setData.append("brush_range", range);
        Document update = new Document();
        update.append("$set", setData);
        collection.updateOne(query, update);
    }
}

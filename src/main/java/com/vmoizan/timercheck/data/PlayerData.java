package com.vmoizan.timercheck.data;

import org.bson.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Data representation class of a player
 */
public class PlayerData {
    /**
     * Default range of a brush
     */
    private final int DEFAULT_BRUSH_RANGE = 5;

    /**
     * The player to whom the data belongs
     */
    private final Player player;

    /**
     * Brush range
     */
    private int brushRange;

    /**
     * Brush material
     */
    private Material brushMaterial;

    /**
     * Constructor to create a default player data
     * @param player: player
     */
    public PlayerData(Player player) {
        this.player = player;
        this.brushMaterial = null;
        this.brushRange = DEFAULT_BRUSH_RANGE;
    }

    /**
     * Constructor to create a player data from a document of the database
     * @param doc: the database document
     * @param player: player
     */
    public PlayerData(Player player, Document doc){
        this.player = player;
        this.brushRange = doc.get("brush_range", DEFAULT_BRUSH_RANGE);
        String materialName = doc.get("brush_material", String.class);
        this.brushMaterial = materialName == null ? null : Material.getMaterial(materialName);

    }

    /**
     * Converts the player data to a database document
     * @return
     */
    public Document toDocument(){
        return new Document()
                .append("uuid", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("brush_range", brushRange)
                .append("brush_material", brushMaterial.name());
    }

    /**
     * @return player
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
     * @return brush range
     */
    public int getBrushRange() {
        return brushRange;
    }

    /**
     * @return brush material
     */
    public Material getBrushMaterial() {
        return brushMaterial;
    }

    /**
     * Set the brush range
     * @param brushRange: range
     */
    public void setBrushRange(int brushRange) {
        this.brushRange = brushRange;
    }

    /**
     * Set the brush material
     * @param brushMaterial: material
     */
    public void setBrushMaterial(Material brushMaterial) {
        this.brushMaterial = brushMaterial;
    }


}
package com.vmoizan.timercheck.data;

import org.bukkit.Material;
import org.bukkit.entity.Player;
public class DataPlayer {

    public Player player;
    public int range = 5;
    public Material brushMaterial;
    public DataPlayer(Player player) {
        this.player = player;
        brushMaterial = Material.RED_WOOL;
    }
}
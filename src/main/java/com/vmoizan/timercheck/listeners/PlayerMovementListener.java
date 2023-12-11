package com.vmoizan.timercheck.listeners;

import com.vmoizan.timercheck.data.DataPlayer;
import com.vmoizan.timercheck.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player p = event.getPlayer();

    }

}

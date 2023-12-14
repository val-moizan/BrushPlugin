package com.vmoizan.timercheck.events;

import com.vmoizan.timercheck.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

/**
 * Class that handles the connection and disconnection of players
 */
public class JoinQuitEvents implements Listener {

    /**
     * Triggered by a player connection
     * @param e: event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        TimerCheck.getInstance().getDataManager().addPlayer(player);
    }
    /**
     * Triggered by a player disconnection
     * @param e: event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        TimerCheck.getInstance().getDataManager().remove(e.getPlayer());
    }
}
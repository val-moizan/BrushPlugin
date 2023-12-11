package com.vmoizan.timercheck.events;

import com.vmoizan.timercheck.TimerCheck;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        TimerCheck.getInstance().getDataManager().add(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        TimerCheck.getInstance().getDataManager().remove(e.getPlayer());
    }
}
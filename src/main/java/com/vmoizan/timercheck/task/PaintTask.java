package com.vmoizan.timercheck.task;

import com.vmoizan.timercheck.TimerCheck;
import com.vmoizan.timercheck.utils.PlayerUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PaintTask extends BukkitRunnable {
    @Override
    public void run(){
        TimerCheck.getInstance().getDataManager().getDataSet().stream().forEach(dataPlayer -> {

        });
    }




}
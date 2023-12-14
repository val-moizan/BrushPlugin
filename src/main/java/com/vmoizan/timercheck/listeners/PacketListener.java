package com.vmoizan.timercheck.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.vmoizan.timercheck.TimerCheck;
import com.vmoizan.timercheck.data.PlayerData;
import com.vmoizan.timercheck.utils.PlayerUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class PacketListener implements Listener {

    public PacketListener(JavaPlugin plugin){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        List<PacketType> listenedPackets = Arrays.asList(PacketType.Play.Client.POSITION,
                PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION_LOOK);
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, listenedPackets) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player p = event.getPlayer();
                PlayerData playerData = TimerCheck.getInstance().getDataManager().getDataPlayer(p);
                if(playerData.getBrushMaterial() == null) return;
                executeSync(plugin, ()->{
                    Block block = PlayerUtils.getLookingBlock(p, playerData.getBrushRange());
                    if(block != null){
                        if(block.getBlockData().getMaterial() != playerData.getBrushMaterial()){
                            block.setType(playerData.getBrushMaterial());
                        }
                    }
                });
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                if(packet.getType() == PacketType.Play.Server.OPEN_SIGN_EDITOR){
                    System.out.println("OPEN SIGN");
                } else if(packet.getType() == PacketType.Play.Server.TILE_ENTITY_DATA){
                    System.out.println("entity data");
                }

            }
        });
    }

    public void executeSync(Plugin plugin, Runnable r){
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, r);
    }
}

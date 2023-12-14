package com.vmoizan.brushplugin.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.vmoizan.brushplugin.BrushPlugin;
import com.vmoizan.brushplugin.data.PlayerData;
import com.vmoizan.brushplugin.utils.PlayerUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * Listener for packets
 */
public class PacketListener implements Listener {

    /**
     * Constructor
     * @param plugin: plugin
     */
    public PacketListener(JavaPlugin plugin){
        startListening(plugin);
    }

    /**
     * Start from listening packets
     * @param plugin: plugin
     */
    private void startListening(JavaPlugin plugin){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        List<PacketType> listenedPackets = Arrays.asList(PacketType.Play.Client.POSITION,
                PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION_LOOK);
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, listenedPackets) {

            /**
             * Triggered for every whitelisted packet recieved
             * @param event: packet event
             */
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player p = event.getPlayer();
                PlayerData playerData = BrushPlugin.getInstance().getDataManager().getDataPlayer(p);
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
        });
    }

    /**
     * Exectute a runnable in sync task
     * @param plugin: plugin
     * @param r: runnable
     */
    public void executeSync(Plugin plugin, Runnable r){
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, r);
    }
}

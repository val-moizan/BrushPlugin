package com.vmoizan.timercheck.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.vmoizan.timercheck.TimerCheck;
import com.vmoizan.timercheck.data.DataPlayer;
import com.vmoizan.timercheck.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketListener implements Listener {

    public PacketListener(JavaPlugin plugin){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        ArrayList<PacketType> listenedPackets = new ArrayList<>(PacketType.Play.Client.getInstance().values());
        listenedPackets.addAll(PacketType.Play.Server.getInstance().values());
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, listenedPackets) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player p = event.getPlayer();
                PacketContainer packet = event.getPacket();
                List<PacketType> packets = Arrays.asList(PacketType.Play.Client.POSITION,
                        PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION_LOOK);
                if(packets.contains(packet.getType())){
                    DataPlayer dataPlayer = TimerCheck.getInstance().getDataManager().getDataPlayer(p);
                    if(dataPlayer.brushMaterial == null) return;
                    executeSync(plugin, ()->{
                        Block block = PlayerUtils.getLookingBlock(p, dataPlayer.range);
                        if(block != null){
                            if(block.getBlockData().getMaterial() != dataPlayer.brushMaterial){
                                block.setType(dataPlayer.brushMaterial);
                            }
                        }
                    });
                }else if(packet.getType() == PacketType.Play.Client.ARM_ANIMATION){
//                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                        public void run() {
//                            ArrayList<RegisteredListener> rls = HandlerList.getRegisteredListeners(plugin);
//                            for (RegisteredListener rl : rls) {
//                                p.sendMessage("------------------");
//                                p.sendMessage(ChatColor.DARK_RED + rl.getListener().getClass().getSimpleName());
//                                Method[] methods = rl.getListener().getClass().getDeclaredMethods();
//                                for (Method m : methods) {
//                                    p.sendMessage(m.getName());
//                                    Type[] params = m.getParameterTypes();
//                                    for (Type pa : params) {
//                                        p.sendMessage("-> " + pa.getTypeName());
//                                    }
//                                }
//                            }
//                        }
//                    });
                }else{
                    executeSync(plugin, ()->{
                        p.sendMessage(packet.getType().name() + "");
                    });
                }
            }

            @Override
            public void onPacketSending(PacketEvent event) {
//                executeSync(plugin, ()->{
//
//                });
            }
        });
    }

    public void executeSync(Plugin plugin, Runnable r){
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                r.run();
            }
        });
    }
}

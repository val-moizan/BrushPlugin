package com.vmoizan.timercheck.guis;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Class that handles the text input from players using sign guis
 * Credits : https://www.spigotmc.org/threads/signmenu-1-16-5-get-player-sign-input.249381/
 * Updated by Gretorm for 1.20
 */
public final class SignMenuFactory {

    /**
     * Plugin
     */
    private final Plugin plugin;

    /**
     * Live map of all opened sign gui
     */
    private final Map<Player, Menu> inputs;

    /**
     * Constructor
     * @param plugin
     */
    public SignMenuFactory(Plugin plugin) {
        this.plugin = plugin;
        this.inputs = new HashMap<>();
        this.listen();
    }

    /**
     * Register a new sign menu
     * @param text: list of string to put on the sign (4 size max)
     * @return the menu
     */
    public Menu newMenu(List<String> text) {
        return new Menu(text);
    }

    /**
     * Listen to incomming update_sign packet to get data when player finished input
     */
    private void listen() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                Menu menu = inputs.remove(player);

                if (menu == null) {
                    return;
                }
                event.setCancelled(true);
                boolean success = menu.response.test(player, event.getPacket().getStringArrays().read(0));

                if (!success && menu.reopenIfFail && !menu.forceClose) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> menu.open(player), 2L);
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) {
                        Location location = menu.location;
                        player.sendBlockChange(location, location.getBlock().getBlockData());
                    }
                }, 2L);
            }
        });
    }

    /**
     * Sign menu class
     */
    public final class Menu {

        /**
         * List of line in the sign (size 4 max)
         */
        private final List<String> text;

        /**
         * Response after player closes sign menu
         */
        private BiPredicate<Player, String[]> response;

        /**
         * reopen the menu if response fail
         */
        private boolean reopenIfFail;

        /**
         * Location of the sign
         */
        private Location location;

        /**
         * Force close the menu (ignore reopen if fail)
         */
        private boolean forceClose;

        /**
         * constructor
         * @param text: List
         */
        Menu(List<String> text) {
            this.text = text;
        }

        /**
         * Set the reopen if fail option
         * @param value: value
         * @return menu
         */
        public Menu reopenIfFail(boolean value) {
            this.reopenIfFail = value;
            return this;
        }

        /**
         * Init the response
         * @param response: value
         * @return menu
         */
        public Menu response(BiPredicate<Player, String[]> response) {
            this.response = response;
            return this;
        }

        /**
         * Open the sign menu to a player
         * @param player: player
         */
        public void open(Player player) {
            Objects.requireNonNull(player, "Tried to open a sign menu to a null player");
            if (!player.isOnline()) {
                return;
            }
            this.location = player.getLocation();
            this.location.setY(this.location.getBlockY() - 5);

            player.sendBlockChange(this.location, Material.OAK_SIGN.createBlockData());
            ;
            player.sendSignChange(
                    this.location,
                    this.text.toArray(new String[4])
            );
            PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            BlockPosition position = new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
            openSign.getBlockPositionModifier().write(0, position);
            openSign.getBooleans().write(0, true); //1.20 update : Allows to see text

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign);
            } catch (InvocationTargetException exception) {
                exception.printStackTrace();
            }
            inputs.put(player, this);
        }

        /**
         * closes the menu. if force is true, the menu will close and will ignore the reopen
         * functionality. false by default.
         *
         * @param player the player
         * @param force decides whether or not it will reopen if reopen is enabled
         */
        public void close(Player player, boolean force) {
            this.forceClose = force;
            if (player.isOnline()) {
                player.closeInventory();
            }
        }
    }
}
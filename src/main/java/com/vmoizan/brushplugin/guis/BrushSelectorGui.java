package com.vmoizan.brushplugin.guis;

import com.vmoizan.brushplugin.*;
import com.vmoizan.brushplugin.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;

/**
 * Gui which allows a player to modify the options of its brush
 * Opened by the /brush command
 */
public class BrushSelectorGui implements Listener {

    /**
     * The inventory object
     */
    private final Inventory inv;

    /**
     * The size of the gui
     */
    private final int GUI_SIZE = 27;

    /**
     * Player
     */
    private final Player player;

    /**
     * Default constructor, a gui is always bind to the player using the /brush command
     * @param player: player
     */
    public BrushSelectorGui(Player player) {
        this.player = player;
        this.inv = Bukkit.createInventory(player, GUI_SIZE, "Brush Selector");
        this.initializeItems();
    }

    /**
     * Init the items inside the gui
     */
    private void initializeItems() {
        for(int i = 0; i < GUI_SIZE; i++){
            if(i == 0){
                ItemStack stack = InventoryUtils.createGuiItem(Material.BRUSH, "Edit brush range");
                inv.setItem(i, stack);
            }else if(i != GUI_SIZE/2){
                ItemStack stack = InventoryUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "");
                inv.setItem(i, stack);
            }
        }
    }



    /**
     * Opens the inventory
     */
    public void openInventory() {
        Material brushMaterial = BrushPlugin.getInstance().getDataManager().getDataPlayer(player).getBrushMaterial();
        inv.setItem(GUI_SIZE/2, InventoryUtils.createGuiItem(brushMaterial, ""));
        player.openInventory(inv);

    }
}

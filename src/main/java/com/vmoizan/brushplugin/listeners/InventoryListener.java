package com.vmoizan.brushplugin.listeners;

import com.vmoizan.brushplugin.*;
import com.vmoizan.brushplugin.data.*;
import com.vmoizan.brushplugin.guis.*;
import com.vmoizan.brushplugin.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.*;

/**
 * Listener for inventory actions
 */
public class InventoryListener implements Listener {

    /**
     * Checks for clicks on items
     * @param e: event
     */
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        InventoryView view = e.getView();
        if(!view.getTitle().equals("Brush Selector")) return;
        e.setCancelled(true);
        final Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        if(inv == null) return;

        int guiSize = inv.getSize();

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        if(inv.equals(p.getInventory())){ //Click on its inventory (bottom inventory)
            onPlayerInventoryClick(e, clickedItem, p);
        }else if(e.getRawSlot() == guiSize/2){ //Click on middle object
            BrushPlugin.getInstance().getDataManager().getDataPlayer(p).setBrushMaterial(null);
            inv.setItem(guiSize/2, null);
            DatabaseUtils.updateBrushMaterial(p, null);
        }else if(e.getRawSlot() == 0){
            editBrushRange(p);
        }

    }

    /**
     * Cancel dragging in our inventory
     * @param e: event
     */
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        e.setCancelled(true);

    }

    /**
     * Called when player clicks on its inventory.
     * Then, we will update its brush material depending on what item he clicked
     * @param e: event
     * @param clickedItem: clicked itemstack
     * @param p: player
     */
    private void onPlayerInventoryClick(InventoryClickEvent e, ItemStack clickedItem, Player p) {
        Inventory topInv = e.getView().getTopInventory();
        int guiSize = topInv.getSize();
        if (!clickedItem.getType().isBlock()) return;
        ItemStack materialStack = p.getInventory().getItem(e.getSlot());
        if (materialStack == null) return;
        Material material = materialStack.getType();
        PlayerData playerData = BrushPlugin.getInstance().getDataManager().getDataPlayer(p);
        if (material != playerData.getBrushMaterial()) {
            playerData.setBrushMaterial(material);
            ItemStack stack = InventoryUtils.createGuiItem(material, "");
            topInv.setItem(guiSize / 2, stack);
            DatabaseUtils.updateBrushMaterial(p, material);
        }
    }

    /**
     * Open the brush range editing gui (sign menu)
     * Also set the response on the player input
     * @param p player
     */
    private void editBrushRange(Player p){
        int currentBrushRange = BrushPlugin.getInstance().getDataManager().getDataPlayer(p).getBrushRange();
        String playerBrushRange = String.valueOf(currentBrushRange);
        SignMenuFactory.Menu menu = BrushPlugin.getInstance().getSignMenuFactory()
                .newMenu(Arrays.asList(playerBrushRange, "^^^^^^", "Please enter", "brush range"))
                .reopenIfFail(false)
                .response((player, strings) -> {
                    try{
                        int newRange = Integer.parseInt(strings[0]);
                        if(newRange < 0){
                            player.sendMessage(ChatColor.RED + "Please enter a number equals or greater than 0");
                            return false;
                        }
                        if(newRange != currentBrushRange){
                            BrushPlugin.getInstance().getDataManager().getDataPlayer(p).setBrushRange(newRange);
                            DatabaseUtils.updateBrushRange(p, newRange);
                            player.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "Successfully set brush range to " + newRange + " blocks");
                        }
                    }catch(NumberFormatException ex){
                        player.sendMessage(ChatColor.RED + "Please enter numbers");
                        return false;
                    }
                    return true;
                });

        menu.open(p);
    }
}

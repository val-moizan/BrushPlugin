package com.vmoizan.timercheck.guis;

import com.vmoizan.timercheck.*;
import com.vmoizan.timercheck.data.*;
import com.vmoizan.timercheck.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.*;

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
     * @param player
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
        Material brushMaterial = TimerCheck.getInstance().getDataManager().getDataPlayer(player).getBrushMaterial();
        inv.setItem(GUI_SIZE/2, InventoryUtils.createGuiItem(brushMaterial, ""));
        player.openInventory(inv);

    }

    /**
     * Checks for clicks on items
     * @param e
     */
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        e.setCancelled(true);
        final Player p = (Player) e.getWhoClicked();
        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        if(e.getClickedInventory().equals(p.getInventory())){
            onPlayerInventoryClick(e, clickedItem, p);
        }else if(e.getRawSlot() == GUI_SIZE/2){
            TimerCheck.getInstance().getDataManager().getDataPlayer(p).setBrushMaterial(null);
            inv.setItem(GUI_SIZE/2, null);
            DatabaseUtils.updateBrushMaterial(p, null);
        }else if(e.getRawSlot() == 0){
            editBrushRange(p);
        }

    }

    /**
     * Cancel dragging in our inventory
     * @param e
     */
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    /**
     * Called when player clicks on its inventory.
     * Then, we will update its brush material depending on what item he clicked
     * @param e: event
     * @param clickedItem: clicked itemstack
     * @param p: player
     */
    private void onPlayerInventoryClick(InventoryClickEvent e, ItemStack clickedItem, Player p){
        if(!clickedItem.getType().isBlock()) return;
        ItemStack materialStack = p.getInventory().getItem(e.getSlot());
        if(materialStack == null) return;
        Material material = materialStack.getType();
        PlayerData playerData = TimerCheck.getInstance().getDataManager().getDataPlayer(p);
        if(material != playerData.getBrushMaterial()){
            playerData.setBrushMaterial(material);
            ItemStack stack = InventoryUtils.createGuiItem(material, "");
            inv.setItem(GUI_SIZE/2, stack);
            DatabaseUtils.updateBrushMaterial(p, material);
        }
    }

    /**
     * Open the brush range editing gui (sign menu)
     * Also set the response on the player input
     * @param p player
     */
    private void editBrushRange(Player p){
        String playerBrushRange = String.valueOf(TimerCheck.getInstance().getDataManager().getDataPlayer(p).getBrushRange());
        SignMenuFactory.Menu menu = TimerCheck.getInstance().getSignMenuFactory()
                .newMenu(Arrays.asList(playerBrushRange, "^^^^^^", "Please enter", "brush range"))
                .reopenIfFail(false)
                .response((player, strings) -> {
                    try{
                        int newRange = Integer.parseInt(strings[0]);
                        TimerCheck.getInstance().getDataManager().getDataPlayer(p).setBrushRange(newRange);
                        DatabaseUtils.updateBrushRange(p, newRange);
                        player.sendMessage(ChatColor.GREEN + "Successfully set brush range to " + newRange + " blocks");
                    }catch(NumberFormatException ex){
                        player.sendMessage(ChatColor.RED + "Please enter numbers");
                        return false;
                    }
                    return true;
                });

        menu.open(p);
    }

    /**
     * Call close functions on inventory close
     * @param e: event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        onClose();
    }

    /**
     * Close function, unregister all listeners
     */
    public void onClose(){
        HandlerList.unregisterAll(this);
    }
}

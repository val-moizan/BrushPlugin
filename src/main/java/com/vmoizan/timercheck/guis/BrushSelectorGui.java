package com.vmoizan.timercheck.guis;

import com.comphenix.protocol.ProtocolLib;
import com.vmoizan.timercheck.TimerCheck;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BrushSelectorGui implements Listener {
    private final Inventory inv;
    private final int GUI_SIZE = 27;
    public BrushSelectorGui() {
        inv = Bukkit.createInventory(null, GUI_SIZE, "Brush Selector");

        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        for(int i = 0; i < GUI_SIZE; i++){
            if(i == 0){
                ItemStack stack = createGuiItem(Material.BRUSH, "Edit brush range");
                inv.setItem(i, stack);
            }else if(i != GUI_SIZE/2){
                ItemStack stack = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "");
                inv.setItem(i, stack);
            }

        }
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        if(material == null) return null;
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final Player ent) {
        ent.openInventory(inv);
        Material brushMaterial = TimerCheck.getInstance().getDataManager().getDataPlayer(ent).brushMaterial;
        inv.setItem(GUI_SIZE/2, createGuiItem(brushMaterial, ""));
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        e.setCancelled(true);
        final Player p = (Player) e.getWhoClicked();
        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        //Si il clique dans l'inventaire du joueur
        if(e.getClickedInventory().equals(p.getInventory())){
            if(!clickedItem.getType().isBlock()) return;
            Material material = p.getInventory().getItem(e.getSlot()).getType();
            TimerCheck.getInstance().getDataManager().getDataPlayer(p).brushMaterial = material;
            ItemStack stack = createGuiItem(material, "");
            inv.setItem(GUI_SIZE/2, stack);
        }else if(e.getRawSlot() == GUI_SIZE/2){
            TimerCheck.getInstance().getDataManager().getDataPlayer(p).brushMaterial = null;
            inv.setItem(GUI_SIZE/2, null);
        }else if(e.getRawSlot() == 0){

            SignMenuFactory.Menu menu = TimerCheck.getInstance().getSignMenuFactory()
                    .newMenu(Arrays.asList("This", "is", "a"))
                    .reopenIfFail(false)
                    .response((player, strings) -> {
                        if (!strings[3].equalsIgnoreCase("sign")) {
                            player.sendMessage("Wrong!");
                            return false;
                        }
                        return true;
                    });

            menu.open(p);
        }

    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        onClose();
    }

    public void onClose(){
        HandlerList.unregisterAll(this);
    }
}

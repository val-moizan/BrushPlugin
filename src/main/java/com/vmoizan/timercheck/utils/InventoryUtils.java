package com.vmoizan.timercheck.utils;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class InventoryUtils {

    /**
     * custo
     * @param material
     * @param name
     * @param lore
     * @return
     */
    public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        if(material == null) return null;
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;
        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
}

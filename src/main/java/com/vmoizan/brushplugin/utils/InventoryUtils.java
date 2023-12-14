package com.vmoizan.brushplugin.utils;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

/**
 * Utils functions about invetory
 */
public class InventoryUtils {

    /**
     * Get an item stack from material, name and lore
     * @param material: material
     * @param name: item name
     * @param lore: lore
     * @return itemstack
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

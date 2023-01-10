package com.newestaf.earthmaputil.util;

import com.newestaf.newestutil.exception.NewestAFException;
import com.newestaf.newestutil.gui.InventoryGUI;
import org.bukkit.plugin.Plugin;

public class InventoryGUIFactory {
    private static Plugin plugin;

    public static void setup(Plugin plugin) {
        InventoryGUIFactory.plugin = plugin;
    }

    public static InventoryGUI createInventoryGUI(String name, int rows) {
        if (plugin == null) {
            throw new NewestAFException("InventoryGUIFactory is not initialized");
        }
        return new InventoryGUI(plugin, name, rows);
    }

    private InventoryGUIFactory() {

    }

}

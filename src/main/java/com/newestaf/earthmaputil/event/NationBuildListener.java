package com.newestaf.earthmaputil.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class NationBuildListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta == null) {
            return;
        }
        if (meta.getLore().contains("Nation Block")) {
            event.setCancelled(true);
            // TODO
        }
    }
}

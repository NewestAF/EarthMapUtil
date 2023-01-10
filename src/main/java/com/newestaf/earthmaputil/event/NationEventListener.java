package com.newestaf.earthmaputil.event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class NationEventListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        ItemMeta meta = event.getItem().getItemMeta();
        if (meta == null || event.getItem().getType() != Material.BEACON || meta.getLore() == null) {
            return;
        }
        // TODO:
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta == null || event.getItemInHand().getType() != Material.BEACON || meta.getLore() == null) {
            return;
        }
        if (meta.getDisplayName().equals(ChatColor.AQUA + "" + ChatColor.BOLD + "수도")) {
            if (meta.getLore().contains("Activated")) {
                // TODO: Extract nation information from lore
                Block block = event.getBlock();
            }
            else {
                event.setCancelled(true);
                // TODO: Explain that you should set the capital property before place it
            }

        }
    }
}

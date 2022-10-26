package com.newestaf.earthmaputil.event;

import com.newestaf.config.ConfigurationManager;
import com.newestaf.config.ConfigurationManager.ConfigurationManagerBuilder;
import com.newestaf.earthmaputil.EarthMapUtil;
import com.newestaf.earthmaputil.util.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ConfigurationManager configManager;

    public PlayerJoinListener() {
        configManager = new ConfigurationManagerBuilder(EarthMapUtil.getInstance())
                .prefix("main")
                .build();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
    }

}

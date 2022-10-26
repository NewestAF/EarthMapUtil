package com.newestaf.earthmaputil;

import com.newestaf.config.ConfigurationListener;
import com.newestaf.config.ConfigurationManager;
import com.newestaf.config.ConfigurationManager.ConfigurationManagerBuilder;
import com.newestaf.earthmaputil.event.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class EarthMapUtil extends JavaPlugin implements ConfigurationListener {


    private static EarthMapUtil instance;
    private ConfigurationManager configManager;

    public static EarthMapUtil getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        setInstance(this);
        configManager = new ConfigurationManagerBuilder(this)
                .listener(this)
                .prefix("main")
                .validate(true)
                .build();

        List<String> locations = new ArrayList<>();
        locations.add("10, 70, 10");
        locations.add("-10, 70, 10");
        locations.add("10, 70, -10");
        configManager.insert("locations", locations);
        saveConfig();

        try {
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDisable() {

    }

    private void setInstance(EarthMapUtil instance) {
        EarthMapUtil.instance = instance;
    }


    @Override
    public Object onConfigurationValidate(
            ConfigurationManager configurationManager,
            String key,
            Object oldVal,
            Object newVal
    ) {
        return null;
    }

    @Override
    public Object onConfigurationChanged(
            ConfigurationManager configurationManager,
            String key,
            Object oldVal,
            Object newVal
    ) {
        return null;
    }
}

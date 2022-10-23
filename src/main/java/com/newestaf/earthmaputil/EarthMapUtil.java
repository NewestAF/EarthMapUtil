package com.newestaf.earthmaputil;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class EarthMapUtil extends JavaPlugin {


    private static EarthMapUtil instance;
    private FileConfiguration config;

    public static EarthMapUtil getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        setInstance(this);

        config = getConfig();

        String[] locations = {
                "10, 70, 10",
                "10, 70, -10",
                "-10, 70, 10",
                "-10, 70, -10",
                "0, 70, 0"
        };

        config.addDefault("locations", locations);
        config.options().copyDefaults(true);
        saveConfig();

    }



    @Override
    public void onDisable() {

    }

    private void

    private void setInstance(EarthMapUtil instance) {
        EarthMapUtil.instance = instance;
    }


}

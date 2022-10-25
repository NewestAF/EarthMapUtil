package com.newestaf.earthmaputil;

import com.newestaf.config.ConfigurationListener;
import com.newestaf.config.ConfigurationManager;
import com.newestaf.config.ConfigurationManager.ConfigurationManagerBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class EarthMapUtil extends JavaPlugin implements ConfigurationListener {


    private static EarthMapUtil instance;
    private ConfigurationManager configurationManager;

    public static EarthMapUtil getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        setInstance(this);
        configurationManager = new ConfigurationManagerBuilder(this)
                .listener(this)
                .prefix("main")
                .validate(true)
                .build();

        String[] locations = {
                "10, 70, 10",
                "10, 70, -10",
                "-10, 70, 10",
                "-10, 70, -10",
                "0, 70, 0"
        };

    }



    @Override
    public void onDisable() {

    }

    private void setInstance(EarthMapUtil instance) {
        EarthMapUtil.instance = instance;
    }


    @Override
    public Object onConfigurationValidate(ConfigurationManager configurationManager, String key, Object oldVal, Object newVal) {
        return null;
    }

    @Override
    public Object onConfigurationChanged(ConfigurationManager configurationManager, String key, Object oldVal, Object newVal) {
        return null;
    }
}

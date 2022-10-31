package com.newestaf.earthmaputil;

import com.newestaf.config.ConfigurationListener;
import com.newestaf.config.ConfigurationManager;
import com.newestaf.config.ConfigurationManager.ConfigurationManagerBuilder;
import com.newestaf.earthmaputil.event.DefaultSpawnListener;
import com.newestaf.earthmaputil.nation.NationManager;
import com.newestaf.earthmaputil.util.DirectoryStructure;
import com.newestaf.util.Debugger;
import com.newestaf.util.LogUtils;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class EarthMapUtil extends JavaPlugin implements ConfigurationListener {


    private static EarthMapUtil instance;
    private ConfigurationManager configManager;

    private NationManager nationManager;

    public static EarthMapUtil getInstance() {
        return instance;
    }

    public static NationManager getNationManager() {
        return getInstance().nationManager;
    }



    @Override
    public void onEnable() {
        setInstance(this);
        setupUtility();
//        initTest();
        registerListeners();

    }

    private void initTest() {
        List<String> locations = new ArrayList<>();
        locations.add("10, 70, 10");
        locations.add("-10, 70, 10");
        locations.add("10, 70, -10");
        configManager.insert("locations", locations);
        saveConfig();
    }

    private void setupUtility() {
        LogUtils.init(this);
        Debugger.getInstance().setTarget(getServer().getConsoleSender());
        configManager = new ConfigurationManagerBuilder(this)
                .listener(this)
                .prefix("main")
                .validate(true)
                .build();
        DirectoryStructure.setup(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new DefaultSpawnListener(), this);
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

package com.newestaf.earthmaputil;

import com.newestaf.earthmaputil.event.DefaultSpawnListener;
import com.newestaf.earthmaputil.nation.NationManager;
import com.newestaf.earthmaputil.util.DirectoryStructure;
import com.newestaf.newestutil.config.ConfigurationManager;
import com.newestaf.newestutil.config.ConfigurationManager.ConfigurationManagerBuilder;
import com.newestaf.newestutil.util.Debugger;
import com.newestaf.newestutil.util.LogUtils;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public final class EarthMapUtil extends JavaPlugin {


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

}

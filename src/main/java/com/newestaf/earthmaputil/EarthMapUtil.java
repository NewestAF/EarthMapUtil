package com.newestaf.earthmaputil;

import co.aikar.commands.PaperCommandManager;
import com.newestaf.earthmaputil.event.DefaultSpawnListener;
import com.newestaf.earthmaputil.nation.Nation;
import com.newestaf.earthmaputil.nation.NationManager;
import com.newestaf.earthmaputil.util.DirectoryStructure;
import com.newestaf.earthmaputil.util.InventoryGUIFactory;
import com.newestaf.newestutil.config.ConfigurationManager;
import com.newestaf.newestutil.config.ConfigurationManager.ConfigurationManagerBuilder;
import com.newestaf.newestutil.util.Debugger;
import com.newestaf.newestutil.util.LogUtils;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;


public final class EarthMapUtil extends JavaPlugin {


    private static EarthMapUtil plugin;
    private ConfigurationManager configManager;

    private NationManager nationManager;
    private final BukkitRunnable autoSave = new BukkitRunnable() {
        @Override
        public void run() {
            saveData();
            getLogger().info("Nation data has been saved.");
        }
    };
    private PaperCommandManager commandManager;

    public static EarthMapUtil getPlugin() {
        return plugin;
    }

    public static NationManager getNationManager() {
        return getPlugin().nationManager;
    }

    public static ConfigurationManager getConfigurationManager() {
        return getPlugin().configManager;
    }


    @Override
    public void onEnable() {
        setInstance(this);
        setupUtility();
        registerListeners();

        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.enableUnstableAPI("help");


        try {
            File nationsFile = new File(DirectoryStructure.getPluginDir() + "nations.json");
            if (nationsFile.createNewFile()) {
                FileWriter writer = new FileWriter(nationsFile);
                writer.write("[]");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadData();
        autoSave.runTaskTimer(this, 6000, 6000);
    }

    @Override
    public void onDisable() {
        autoSave.cancel();
        saveData();
    }

    private void setupUtility() {
        LogUtils.init(this);
        Debugger.getInstance().setTarget(getServer().getConsoleSender());
        configManager = new ConfigurationManagerBuilder(this)
                .prefix("main")
                .validate(true)
                .build();
        DirectoryStructure.setup(this);
        InventoryGUIFactory.setup(this);
        nationManager = new NationManager();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new DefaultSpawnListener(), this);
    }

    public void saveData() {
        JSONArray jsonNations = new JSONArray();
        for (Nation nation : nationManager.getNations().values()) {
            //noinspection unchecked
            jsonNations.add(nation.toJSON());
        }
        FileWriter nationWriter = null;
        try {
            nationWriter = new FileWriter(DirectoryStructure.getPluginDir() + "nations.json");
            nationWriter.write(jsonNations.toJSONString());
        }
        catch (IOException e) {
            getLogger().info("IOException: " + e.getMessage());
        }
        finally {
            try {
                Objects.requireNonNull(nationWriter).flush();
                nationWriter.close();
            } catch(IOException e) {
                getLogger().info("IOException: " + e.getMessage());
            }
        }

    }

    private void loadData() {
        JSONParser jsonParser = new JSONParser();

        try {
            JSONArray jsonNations = (JSONArray) jsonParser.parse(new FileReader(DirectoryStructure.getPluginDir() + "nations.json"));
            for (Object nation : jsonNations) {
                nationManager.addNation(new Nation((JSONObject) nation));
            }
        }
        catch (IOException e) {
            getLogger().info("IOException: " + e.getMessage());
        }
        catch (ParseException e) {
            getLogger().info("ParseException: " + e.getMessage());
        }

    }






    private void setInstance(EarthMapUtil instance) {
        EarthMapUtil.plugin = instance;
    }

}

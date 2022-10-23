package com.newestaf.earthmaputil.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private final Plugin plugin;
    private final Configuration config;
    private final Configuration descConfig;
    private final Map<String,Class<?>> forceTypes = new HashMap<String,Class<?>>();

    private ConfigurationListener listener;
    private String prefix;
    private boolean validate = true;

    public ConfigurationManager(Plugin plugin, ConfigurationListener listener) {
        this.plugin = plugin;
        this.prefix = null;
        this.listener = listener;

        this.config = plugin.getConfig();
        config.options().copyDefaults(true);

        this.descConfig = new MemoryConfiguration();

        plugin.saveConfig();
    }

    public ConfigurationManager(Plugin plugin) {
        this(plugin, null);
    }

    public ConfigurationManager(Configuration config) {
        this.plugin = null;
        this.prefix = null;
        this.listener = null;
        this.config = config;
        this.descConfig = new MemoryConfiguration();
    }

    //region Getter, Setter
    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfigurationListener(ConfigurationListener listener) {
        this.listener = listener;
    }

    public void forceType(String key, Class<?> c) {
        forceTypes.put(key, c);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    //endregion

    



}

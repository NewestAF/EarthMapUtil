package com.newestaf.earthmaputil.util;

import com.newestaf.earthmaputil.EarthMapUtil;

import java.io.File;

public class DirectoryStructure {
    private static final File pluginDir = new File("plugins", "EarthMapUtil");

    private static File databaseDir;

    private static final String databaseFolderName = "data";

    public static File getDatabaseDir() {
        return databaseDir;
    }

    public static void setup(EarthMapUtil plugin) {
        setupDirectoryStructure();

    }

    private static void setupDirectoryStructure() {
        databaseDir = new File(pluginDir, databaseFolderName);

        createDir(databaseDir);

    }

    private static void createDir(File dir) {
        if (dir.isDirectory()) {
            return;
        }
        if (!dir.mkdir()) {
            EarthMapUtil.getInstance().getLogger().warning("Can't make directory " + dir.getName());
        }
    }

    public static File getResourceFileForSave(File dir, String filename) {
        return new File(dir, "custom" + File.separator + filename.toLowerCase() + ".yml");
    }

}

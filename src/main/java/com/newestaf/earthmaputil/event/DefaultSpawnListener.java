package com.newestaf.earthmaputil.event;

import com.newestaf.config.ConfigurationManager;
import com.newestaf.config.ConfigurationManager.ConfigurationManagerBuilder;
import com.newestaf.earthmaputil.EarthMapUtil;
import com.newestaf.earthmaputil.util.DatabaseManager;
import com.newestaf.earthmaputil.util.DirectoryStructure;
import com.newestaf.earthmaputil.util.SQLCondition;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultSpawnListener implements Listener {

    private final ConfigurationManager configManager;


    public DefaultSpawnListener(){
        configManager = new ConfigurationManagerBuilder(EarthMapUtil.getInstance()).prefix("main")
                .build();
        try (DatabaseManager db = new DatabaseManager(DirectoryStructure.getDatabaseDir(), "players")) {
            db.createTable(
                    "start_position",
                    "uuid VARCHAR(36) NOT NULL",
                    "x DOUBLE NOT NULL",
                    "y DOUBLE NOT NULL",
                    "z DOUBLE NOT NULL"
            );
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        try (
                DatabaseManager db = new DatabaseManager(DirectoryStructure.getDatabaseDir(), "players")
        ) {
            if (!db.select(
                    "start_position",
                    new SQLCondition("uuid", player.getUniqueId().toString(), SQLCondition.ConditionType.EQUALS)
            ).next()) {

                @SuppressWarnings("unchecked")
                List<String> rawLocations = ((List<String>) configManager.get("locations"));
                String rawLocation = rawLocations.get(ThreadLocalRandom.current().nextInt(rawLocations.size()));
                String[] split = rawLocation.split(", ");

                Location location = new Location(
                        player.getWorld(),
                        Double.parseDouble(split[0]),
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2])
                );

                player.teleport(location);

                db.insert(
                        "start_position",
                        player.getUniqueId().toString(),
                        String.valueOf(location.getX()),
                        String.valueOf(location.getY()),
                        String.valueOf(location.getZ())
                );
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.isBedSpawn()) {
            return;
        }
        try (
                DatabaseManager db = new DatabaseManager(DirectoryStructure.getDatabaseDir(), "players")
        ) {

            ResultSet resultSet = db.select(
                    "start_position",
                    new SQLCondition(
                            "uuid",
                            event.getPlayer().getUniqueId().toString(),
                            SQLCondition.ConditionType.EQUALS
                    )
            );

            resultSet.next();

            Location location = new Location(
                    event.getPlayer().getWorld(),
                    resultSet.getDouble("x"),
                    resultSet.getDouble("y"),
                    resultSet.getDouble("z")
            );

            event.setRespawnLocation(location);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();

        }
    }

}

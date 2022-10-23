package com.newestaf.earthmaputil.util;

import com.newestaf.earthmaputil.EarthMapUtil;

import java.io.File;
import java.sql.*;


public class DatabaseManager {

    private static Connection connection;

    public DatabaseManager() throws ClassNotFoundException, SQLException {
        makeDBConnection();

    }

    private void makeDBConnection() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        File dbFile = new File(DirectoryStructure.getDatabaseDir(), "data.db");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath() + File.separator + "data.db");
    }



    public static Connection getConnection() throws SQLException {
        return connection;

    }

    private void createTableIfNotExists(String tableName, String ddl) throws SQLException {
        String fullName = EarthMapUtil.getInstance().getConfig().getString("database.table_prefix", "chesscraft_") + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(tableName)) {
                stmt.executeUpdate("ALTER TABLE " + tableName + " RENAME TO " + fullName);
                EarthMapUtil.getInstance().getLogger().info("renamed DB table " + tableName + " to " + fullName);
            } else if (!tableExists(fullName)) {
                stmt.executeUpdate("CREATE TABLE " + fullName + "(" + ddl + ")");
            }
        } catch (SQLException e) {
            EarthMapUtil.getInstance().getLogger().warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    private boolean tableExists(String table) throws SQLException {
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null , null, table, null);
        return tables.next();
    }

}

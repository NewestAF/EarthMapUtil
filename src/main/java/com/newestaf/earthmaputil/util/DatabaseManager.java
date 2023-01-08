package com.newestaf.earthmaputil.util;

import com.newestaf.newestutil.exception.NewestAFException;
import com.newestaf.newestutil.util.Debugger;
import com.newestaf.newestutil.util.LogUtils;
import java.io.File;
import java.sql.*;


public class DatabaseManager implements AutoCloseable {

    private Connection connection;
    private final String prefix;

    public DatabaseManager(File dbFile, String prefix) throws ClassNotFoundException, SQLException {
        this.prefix = prefix;
        makeDBConnection(dbFile);
    }

    public static String handleColumnsOrValues(String... columnsOrValues) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < columnsOrValues.length; i++) {
            String columnOrValue = columnsOrValues[i];
            if (i == columnsOrValues.length - 1) {
                builder.append(columnOrValue);
            }
            else {
                builder.append(columnOrValue).append(", ");
            }
        }
        return builder.toString();
    }

    public void createTable(String tableName, String columns) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (!tableExists(fullName)) {
                stmt.executeUpdate("CREATE TABLE " + fullName + "(" + columns + ")");
            }
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public void insert(String tableName, String values) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                String query = "INSERT INTO " + fullName + " VALUES (" + values + ")";
                stmt.executeUpdate(query);
            }
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public void insert(String tableName, String columns, String values) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                stmt.executeUpdate("INSERT INTO " + fullName + "(" + columns + ")"
                                           + " VALUES (" + values + ")");
            }
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean isExist(String tableName, SQLCondition predicates) {
        String fullName = prefix + tableName;
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT EXISTS(SELECT 1 FROM " + fullName + " WHERE " + predicates + ") as success";
            stmt.execute(query);
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet.next()) {
                return resultSet.getBoolean("success");
            }
            return false;
        }
        catch (SQLException e) {
            throw new NewestAFException("cant execute");
        }
    }

    public ResultSet select(String tableName, SQLCondition predicates) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                String query = "SELECT " + " * " + " FROM " + fullName + " WHERE " + predicates;
                stmt.execute(query);
                return stmt.getResultSet();
            }
            else {
                throw new NewestAFException("table " + fullName + " doesn't exist");
            }

        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public ResultSet select(String tableName, String columns, SQLCondition predicates) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                String query = "SELECT " + columns + " FROM " + fullName + " WHERE " + predicates;
                stmt.execute(query);
            }
            return stmt.getResultSet();
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    private void shutdown() {
        try {
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
            Debugger.getInstance()
                    .debug("Closing DB Connection to " + connection.getMetaData().getDatabaseProductName());
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeDBConnection(File dbFile) throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath() + File.separator + "data.db");
    }

    private boolean tableExists(String table) throws SQLException {
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, table, null);
        return tables.next();
    }

    @Override
    public void close() {
        shutdown();
    }
}

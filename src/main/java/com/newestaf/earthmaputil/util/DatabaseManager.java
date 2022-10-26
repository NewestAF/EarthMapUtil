package com.newestaf.earthmaputil.util;

import com.newestaf.util.Debugger;
import com.newestaf.util.LogUtils;

import java.io.File;
import java.sql.*;


public class DatabaseManager implements AutoCloseable {

    private Connection connection;
    private final String prefix;

    public DatabaseManager(File dbFile, String prefix) throws ClassNotFoundException, SQLException {
        this.prefix = prefix;
        makeDBConnection(dbFile);
    }

    public void createTable(String tableName, String... column) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(tableName)) {
                stmt.executeUpdate("ALTER TABLE " + tableName + " RENAME TO " + fullName);
                LogUtils.info("renamed DB table " + tableName + " to " + fullName);
            }
            else if (!tableExists(fullName)) {
                StringBuilder sb = new StringBuilder();
                for (String s : column) {
                    sb.append(s).append(", ");
                }
                stmt.executeUpdate("CREATE TABLE " + fullName + "(" + sb + ")");
            }
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public void insert(String tableName, String... values) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                StringBuilder sb = new StringBuilder();
                for (String s : values) {
                    sb.append(s).append(", ");
                }
                stmt.executeUpdate("INSERT INTO " + fullName + " VALUES (" + sb + ")");
            }
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public void insert(String tableName, String[] columns, String[] values) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                StringBuilder valueStringBuilder = new StringBuilder();
                StringBuilder columnsStringBuilder = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    valueStringBuilder.append(values[i]).append(", ");
                    columnsStringBuilder.append(columns[i]).append(", ");
                }
                stmt.executeUpdate("INSERT INTO " + fullName + "(" + columnsStringBuilder + ")"
                                           + " VALUES (" + valueStringBuilder + ")");
            }
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public ResultSet select(String tableName, SQLCondition predicates) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                stmt.executeUpdate("SELECT " + " * " + " FROM " + fullName + " WHERE " + predicates);
            }
            return stmt.getResultSet();
        }
        catch (SQLException e) {
            LogUtils.warning("can't execute " + stmt + ": " + e.getMessage());
            throw e;
        }
    }

    public ResultSet select(String tableName, String[] columns, SQLCondition predicates) throws SQLException {
        String fullName = prefix + tableName;
        Statement stmt = connection.createStatement();
        try {
            if (tableExists(fullName)) {
                StringBuilder columnsStringBuilder = new StringBuilder();
                for (String s : columns) {
                    columnsStringBuilder.append(s).append(", ");
                }
                stmt.executeUpdate("SELECT " + columnsStringBuilder + " FROM " + fullName + " WHERE " + predicates);
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

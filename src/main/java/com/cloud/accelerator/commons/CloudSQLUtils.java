package com.cloud.accelerator.commons;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CloudSQLUtils {

    private static DataSource setupMySQLConnection() throws IOException {
        String CLOUD_MYSQL_CONNECTION_NAME = GenericUtils.readProps("CLOUD_MYSQL_CONNECTION_NAME");
        String MY_SQL_USER = GenericUtils.readProps("MY_SQL_USER");
        String MY_SQL__PASS = GenericUtils.readProps("MY_SQL__PASS");
        String MY_SQL_DB_NAME = GenericUtils.readProps("MY_SQL_DB_NAME");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql:///%s", MY_SQL_DB_NAME));
        config.setUsername(MY_SQL_USER);
        config.setPassword(MY_SQL__PASS);
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", CLOUD_MYSQL_CONNECTION_NAME);
        config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        DataSource pool = new HikariDataSource(config);
        return pool;
    }

    public static DataSource setupSQLServerConnection() throws SQLException, IOException {
        String SQLSERVER_USER = GenericUtils.readProps("SQLSERVER_USER");
        String SQLSERVER_PASS = GenericUtils.readProps("SQLSERVER_PASS");
        String SQLSERVER_DB_NAME = GenericUtils.readProps("SQLSERVER_DB_NAME");

        HikariConfig config = new HikariConfig();
        config
                .setDataSourceClassName(String.format("com.microsoft.sqlserver.jdbc.SQLServerDataSource"));
        config.setUsername(SQLSERVER_USER);
        config.setPassword(SQLSERVER_PASS);
        config.addDataSourceProperty("databaseName",SQLSERVER_DB_NAME);
        config.addDataSourceProperty("socketFactoryClass",
                "com.google.cloud.sql.sqlserver.SocketFactory");
        config.addDataSourceProperty("socketFactoryConstructorArg",
                System.getenv("SQLSERVER_CONNECTION_NAME"));

        DataSource pool = new HikariDataSource(config);
        return pool;
    }

    public static DataSource setupPostGresConnection() throws IOException {
        String CLOUD_POSTGres_CONNECTION_NAME = GenericUtils.readProps("CLOUD_POSTGres_CONNECTION_NAME");
        String POSTGres_USER = GenericUtils.readProps("POSTGres_USER");
        String POSTGres_PASS = GenericUtils.readProps("POSTGres_PASS");
        String POSTGres_DB_NAME = GenericUtils.readProps("POSTGres_DB_NAME");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql:///%s", POSTGres_DB_NAME));
        config.setUsername(POSTGres_USER);
        config.setPassword(POSTGres_PASS);
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", CLOUD_POSTGres_CONNECTION_NAME);
        config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(10000); // 10 seconds
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes
        DataSource pool = new HikariDataSource(config);
        return pool;
    }

    public static void createMySQLTable(String SQL) throws IOException {
        DataSource pool = setupMySQLConnection();
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement createTableStatement = conn.prepareStatement(SQL);) {
                createTableStatement.execute();
            }
        } catch (SQLException ex) {

        }
    }

    public static void createPostGresTable(String SQL) throws IOException {
        DataSource pool = setupPostGresConnection();
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement createTableStatement = conn.prepareStatement(SQL);) {
                createTableStatement.execute();
            }
        } catch (SQLException ex) {

        }
    }

    public static void createSQLServerTable(String SQL) throws IOException, SQLException {
        DataSource pool = setupSQLServerConnection();
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement createTableStatement = conn.prepareStatement(SQL);) {
                createTableStatement.execute();
            }
        } catch (SQLException ex) {

        }
    }

    public static void dropTableMySQL(String tableName) throws SQLException, IOException {
        DataSource pool = setupMySQLConnection();
        try (Connection conn = pool.getConnection()) {
            String stmt = String.format("DROP TABLE %s;", tableName);
            try (PreparedStatement dropTableStatement = conn.prepareStatement(stmt)) {
                dropTableStatement.execute();
            }
        }
    }

    public static void dropTablePostGres(String tableName) throws SQLException, IOException {
        DataSource pool = setupPostGresConnection();
        try (Connection conn = pool.getConnection()) {
            String stmt = String.format("DROP TABLE %s;", tableName);
            try (PreparedStatement dropTableStatement = conn.prepareStatement(stmt)) {
                dropTableStatement.execute();
            }
        }
    }

    public static void dropTableSQLServer(String tableName) throws SQLException, IOException {
        DataSource pool = setupSQLServerConnection();
        try (Connection conn = pool.getConnection()) {
            String stmt = String.format("DROP TABLE %s;", tableName);
            try (PreparedStatement dropTableStatement = conn.prepareStatement(stmt)) {
                dropTableStatement.execute();
            }
        }
    }

    public static void getDataMySQL(String SQL) throws IOException {
        DataSource pool = setupMySQLConnection();
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement sqlStmt = conn.prepareStatement(SQL);) {
                ResultSet voteResults = sqlStmt.executeQuery();
                System.out.println(voteResults);
            }
        } catch (SQLException ex) {
        }
    }

    public static void getDataPostGres(String SQL) throws IOException {
        DataSource pool = setupPostGresConnection();
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement sqlStmt = conn.prepareStatement(SQL);) {
                ResultSet voteResults = sqlStmt.executeQuery();
                System.out.println(voteResults);
            }
        } catch (SQLException ex) {
        }
    }

    public static void getDataSQLServer(String SQL) throws IOException, SQLException {
        DataSource pool = setupSQLServerConnection();
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement sqlStmt = conn.prepareStatement(SQL);) {
                ResultSet voteResults = sqlStmt.executeQuery();
                System.out.println(voteResults);
            }
        } catch (SQLException ex) {
        }
    }


}

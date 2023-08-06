package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.CloudSQLUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;
import java.sql.SQLException;

public class CloudSqlStepDef {

    @Given("Create cloud MYSQL table using {string}")
    public void createMySQLTable(String query) throws IOException {
        CloudSQLUtils.createMySQLTable(query);
    }

    @And("Create cloud PostGres table using {string}")
    public void createPostGresTable(String query) throws IOException {
        CloudSQLUtils.createPostGresTable(query);
    }

    @And("Create cloud SQL Sever table using {string}")
    public void createSQLSeverTable(String query) throws IOException, SQLException {
        CloudSQLUtils.createSQLServerTable(query);
    }

    @And("Drop cloud MYSQL table {string}")
    public void dropMySQLTable(String TableName) throws IOException, SQLException {
        CloudSQLUtils.dropTableMySQL(TableName);
    }

    @And("Drop cloud PostGres table {string}")
    public void dropPostGresTable(String TableName) throws IOException, SQLException {
        CloudSQLUtils.dropTablePostGres(TableName);
    }

    @And("Drop cloud SQL Sever table {string}")
    public void dropSQLSeverTable(String TableName) throws IOException, SQLException {
        CloudSQLUtils.dropTableSQLServer(TableName);
    }

    @And("Get records from cloud MYSQL table using {string}")
    public void getDataMySQLTable(String query) throws IOException {
        CloudSQLUtils.getDataMySQL(query);
    }

    @And("Get records from cloud PostGres table using {string}")
    public void getDataPostGresTable(String query) throws IOException {
        CloudSQLUtils.getDataPostGres(query);
    }

    @And("Get records from cloud SQL Sever table using {string}")
    public void getDataSQLSeverTable(String query) throws IOException, SQLException {
        CloudSQLUtils.getDataSQLServer(query);
    }


}

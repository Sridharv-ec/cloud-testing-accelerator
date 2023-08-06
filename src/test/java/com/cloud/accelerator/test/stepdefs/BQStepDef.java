package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.BigQueryUtils;
import com.cloud.accelerator.commons.GenericUtils;
import com.google.cloud.bigquery.Schema;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;

public class BQStepDef {

    String projectId = GenericUtils.readProps("projectId");

    public BQStepDef() throws IOException {
    }

    @Given("Get data from BQ table {string}")
    public void getBQResult(String tableName) throws IOException {
        String Config_table = GenericUtils.readProps(tableName);
        BigQueryUtils.executeQuery("SELECT * FROM `" + Config_table + "`");
    }

    @Then("Add columns {string} to dataset {string} and table {string}")
    public void addEmptyColumn(String columnName, String dataSetName, String tableName) throws IOException {
        BigQueryUtils.addEmptyColumn(columnName, dataSetName, tableName);
    }

    @Then("Verify if dataset {string} exists in BigQuery")
    public void datasetExists(String dataSetName) {
        BigQueryUtils.datasetExists(dataSetName);
    }

    @Then("Create new dataset {string}")
    public void createDataset(String dataSetName) {
        BigQueryUtils.createDataset(dataSetName);
    }

    @Then("Create new table with dataset {string} table {string} and schema {string}")
    public void createTable(String dataSetName, String tableName, Schema schema) {
        BigQueryUtils.createTable(dataSetName, tableName, schema);
    }

    @Then("Delete dataset {string}")
    public void deleteDataset(String datasetName) throws IOException {
        BigQueryUtils.deleteDataset(projectId, datasetName);
    }

    @Then("Delete table {string} from dataset {string}")
    public void deleteTable(String tableName, String datasetName) {
        BigQueryUtils.deleteTable(tableName, datasetName);
    }

    @Then("Get dataset information for dataset {string}")
    public void getDatasetInfo(String datasetName) throws IOException {
        BigQueryUtils.getDatasetInfo(projectId, datasetName);
    }

    @Then("Get properties for {string} table in {string} dataset")
    public void getTableProperties(String tableName, String datasetName) throws IOException {
        BigQueryUtils.getTableProperties(projectId, datasetName, tableName);
    }

    @Then("Get record count for {string} from BQ table")
    public void queryTotalRows(String data) throws IOException {
        String query = GenericUtils.readProps(data);
        BigQueryUtils.queryTotalRows(query);
    }

    @Then("Get list of all tables in {string} dataset")
    public void listTables(String datasetName) {
        BigQueryUtils.listTables(projectId, datasetName);
    }

    @Then("Get data from partitioned BQ table for {string} having start_date {string} and end_date {string}")
    public void queryPartitionedTable(String query, String start_date, String end_date) {
        BigQueryUtils.queryPartitionedTable(query, start_date, end_date);
    }



}
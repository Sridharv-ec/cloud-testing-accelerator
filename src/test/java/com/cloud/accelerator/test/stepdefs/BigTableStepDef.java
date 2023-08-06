package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.BigTableUtils;
import com.cloud.accelerator.commons.GenericUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;

public class BigTableStepDef {

    String projectId = GenericUtils.readProps("projectId");
    String instanceId = GenericUtils.readProps("bigTable_instanceId");

    public BigTableStepDef() throws IOException {
    }

    @Given("Create Big table {string} with column {string}")
    public void createBigTable(String tableId, String column_family) throws IOException {
        BigTableUtils.createTable(projectId, instanceId, tableId, column_family);
    }

    @And("Read data from Big table {string}")
    public void readBigTable(String tableId) throws IOException {
        BigTableUtils.readTable(projectId, instanceId, tableId);
    }

    @And("Write data to big table {string} with column {string}")
    public void writeToTable(String tableId) throws IOException {
        BigTableUtils.writeToTable(projectId, instanceId, tableId);
    }


}

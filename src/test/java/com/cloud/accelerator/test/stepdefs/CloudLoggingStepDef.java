package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.CloudLoggingUtils;
import com.cloud.accelerator.commons.GenericUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;

public class CloudLoggingStepDef {

    @Given("Create sink {string} for dataset {string}")
    public void createSink(String sinkName, String datasetName) throws IOException {
        datasetName = GenericUtils.readProps(datasetName);
        CloudLoggingUtils.createSink(sinkName, datasetName);
    }

    @And("Update sink {string} for dataset {string}")
    public void updateSink(String sinkName, String datasetName) throws IOException {
        datasetName = GenericUtils.readProps(datasetName);
        CloudLoggingUtils.updateSink(sinkName, datasetName);
    }

    @And("List all sink")
    public void listSinks() {
        CloudLoggingUtils.listSinks();
    }

    @And("Delete sink {string}")
    public void deleteSink(String sinkName) {
        CloudLoggingUtils.deleteSink(sinkName);
    }

    @And("Delete log {string}")
    public void deleteLog(String logName) {
        CloudLoggingUtils.deleteLog(logName);
    }

    @And("List log {string}")
    public void listLogEntries(String filter) {
        CloudLoggingUtils.listLogEntries(filter);
    }

}

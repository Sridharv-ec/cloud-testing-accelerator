package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.CloudMonitoringUtils;
import com.cloud.accelerator.commons.GenericUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;

public class CloudMonitoringStepDef {

    String projectId = GenericUtils.readProps("projectId");

    public CloudMonitoringStepDef() throws IOException {
    }

        @Given("List all alert policies for the project")
    public void listAlertPolicies() throws IOException {
        CloudMonitoringUtils.listAlertPolicies( projectId);
    }

    @And("Get all alert policies")
    public void getAlertPolicies() throws IOException {
        CloudMonitoringUtils.getAlertPolicies( projectId);
    }

    @And("Enable policy for filter {string}")
    public void enablePolicies(String filter) throws IOException {
        CloudMonitoringUtils.enablePolicies( projectId, filter ,true);
    }

}

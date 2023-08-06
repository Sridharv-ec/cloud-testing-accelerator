package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.DataProcUtils;
import com.cloud.accelerator.commons.GenericUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import java.io.IOException;

public class DataProcStepDef {

    String projectId = GenericUtils.readProps("projectId");
    String region = GenericUtils.readProps("region");

    public DataProcStepDef() throws IOException {
    }

    @Given("Create data proc cluster {string} with autoscaling policy {string}")
    public void createClusterwithAutoscaling(String clusterName, String autoscalingPolicyName) throws IOException, InterruptedException {
        DataProcUtils.createClusterwithAutoscaling(projectId, region, clusterName, autoscalingPolicyName);
    }

    @And("Create data proc cluster {string}")
    public void createCluster(String clusterName) throws IOException, InterruptedException {
        DataProcUtils.createCluster(projectId, region, clusterName);
    }

    @And("Submit data proc job for cluster {string}")
    public void submitJob(String clusterName) throws IOException, InterruptedException {
        DataProcUtils.submitJob(projectId, region, clusterName);
    }
}

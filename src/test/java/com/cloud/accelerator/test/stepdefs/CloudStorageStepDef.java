package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.CloudStorageUtils;
import com.cloud.accelerator.commons.GenericUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CloudStorageStepDef {

    String projectId = GenericUtils.readProps("projectId");

    public CloudStorageStepDef() throws IOException {
    }

    @Given("Copy object {string} from {string} to {string}")
    public void copyObject(String objectName, String sourceBucketName, String targetBucketName) {
        CloudStorageUtils.copyObject(projectId, sourceBucketName, objectName, targetBucketName);
    }

    @Then("Create bucket {string}")
    public void createBucket(String bucketName) {
        CloudStorageUtils.createBucket(projectId, bucketName);
    }

    @Then("Delete bucket {string}")
    public void deleteBucket(String bucketName) {
        CloudStorageUtils.deleteBucket(projectId, bucketName);
    }

    @Then("Upload {string} Object into {string} bucket")
    public void uploadObject(String objectName, String bucketName) throws IOException {
        bucketName = GenericUtils.readProps(bucketName);
        Path userDir = Paths.get(System.getProperty("user.dir"));
        String filePath = userDir + "/src/test/resources/test_data/" + objectName;
        System.out.println(projectId + " bucketName: " + bucketName + " filePath:  " + filePath);
        CloudStorageUtils.uploadObject(projectId, bucketName, objectName, filePath);
    }

}

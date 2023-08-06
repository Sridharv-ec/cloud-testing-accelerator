package com.cloud.accelerator.test.stepdefs;
import com.cloud.accelerator.commons.CloudFunctionUtils;
import com.cloud.accelerator.commons.GenericUtils;
import io.cucumber.java.en.Given;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CloudFunctionStepDef {

    @Given("Create proto for schema {string} with fileName {string}")
    public void createProtoSchema( String schemaId, String fileName) throws IOException {
        Path userDir = Paths.get(System.getProperty("user.dir"));
        String projectId = GenericUtils.readProps("projectId");
        String protoFile = userDir +"/src/test/resources/test_data/data_csv/"+fileName;
        CloudFunctionUtils.createProtoSchema(projectId, schemaId, protoFile);
    }

}

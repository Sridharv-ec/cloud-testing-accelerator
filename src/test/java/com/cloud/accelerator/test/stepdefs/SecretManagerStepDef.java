package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.GenericUtils;
import com.cloud.accelerator.commons.SecretManagerUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;

public class SecretManagerStepDef {

    String projectId = GenericUtils.readProps("projectId");

    public SecretManagerStepDef() throws IOException {
    }

    @Given("Get secret {string}")
    public void getSecret(String secretId) throws IOException {
        SecretManagerUtils.getSecret(projectId, secretId);
    }

    @And("Create secret {string}")
    public void createSecret(String secretId) throws IOException {
        SecretManagerUtils.createSecret(projectId, secretId);
    }

    @And("Grant member {string} access to {string}")
    public void iamGrantAccess(String member, String secretId) throws IOException {
        SecretManagerUtils.iamGrantAccess(projectId, secretId, member);
    }

    @And("List all secret for the project")
    public void listSecrets() throws IOException {
        SecretManagerUtils.listSecrets(projectId);
    }
}

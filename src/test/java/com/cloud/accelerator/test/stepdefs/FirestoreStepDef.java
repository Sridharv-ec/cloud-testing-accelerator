package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.FirestoreUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class FirestoreStepDef {

    @Given("Get document result as map for collection {string} document {string}")
    public void getDocumentAsMap(String collectionName, String documentName) throws Exception {
        FirestoreUtils.getDocumentAsMap(collectionName, documentName);
    }

    @And("Get query result for collection {string} document {string}")
    public void getQueryResults(String collectionName, String documentName) throws Exception {
        FirestoreUtils.getQueryResults(collectionName, documentName);
    }

    @And("Get all records for collection {string}")
    public void getAllDocuments(String collectionName) throws Exception {
        FirestoreUtils.getAllDocuments(collectionName);
    }


}

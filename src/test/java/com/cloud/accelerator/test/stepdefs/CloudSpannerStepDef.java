package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.CloudSpannerUtils;
import com.google.cloud.spanner.DatabaseId;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import java.io.IOException;

public class CloudSpannerStepDef {

    @Given("Create spanner DB {string} for SQL {string}")
    public void createDatabase(DatabaseId id, String sql) throws IOException { CloudSpannerUtils.createDatabase(id, sql); }

    @And("Query spanner DB and fetch {string} using SQL {string}")
    public void queryTable(String fieldName, String sql) throws IOException { CloudSpannerUtils.queryTable(sql, fieldName); }

    @And("Insert into spanner DB using {string}")
    public void insertUsingDml(String sql) throws IOException {
        CloudSpannerUtils.insertUsingDml(sql);
    }

    @And("Update data in spanner DB using {string}")
    public void updateUsingDml(String sql) throws IOException {
        CloudSpannerUtils.updateUsingDml(sql);
    }

    @And("Delete data from spanner DB using {string}")
    public void deleteUsingDml(String sql) throws IOException {
        CloudSpannerUtils.deleteUsingDml(sql);
    }

}

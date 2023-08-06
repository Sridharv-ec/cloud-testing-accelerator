package com.cloud.accelerator.commons;

import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BigQueryUtils {

    static BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

    public static void executeQuery(String sql) {
        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(sql).setUseLegacySql(false).build();
            JobId jobId = JobId.of(UUID.randomUUID().toString());
            Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
            queryJob = queryJob.waitFor();

            if (queryJob == null) {
                throw new RuntimeException("\n Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                throw new RuntimeException(queryJob.getStatus().getError().toString());
            }

            // Get the results.
            TableResult result = queryJob.getQueryResults();
            result.iterateAll().forEach(row -> row.forEach(val -> System.out.printf("\n %s,", val.toString())));
            System.out.println("\n BQ query executed successfully !!!");
        } catch (BigQueryException | InterruptedException e) {
            System.out.println("\n BQ Query not performed " + e.toString());
        }
    }

    public static void addEmptyColumn(String newColumnName, String datasetName, String tableId) {
        try {

            Table table = bigquery.getTable(datasetName, tableId);
            Schema schema = table.getDefinition().getSchema();
            FieldList fields = schema.getFields();
            // Create the new field/column
            Field newField = Field.of(newColumnName, LegacySQLTypeName.STRING);

            // Create a new schema adding the current fields, plus the new one
            List<Field> fieldList = new ArrayList<Field>();
            fields.forEach(fieldList::add);
            fieldList.add(newField);
            Schema newSchema = Schema.of(fieldList);

            // Update the table with the new schema
            Table updatedTable =
                    table.toBuilder().setDefinition(StandardTableDefinition.of(newSchema)).build();
            updatedTable.update();
            System.out.println("Empty column successfully added to table");
        } catch (BigQueryException e) {
            System.out.println("Empty column was not added. \n" + e.toString());
        }
    }

    public static void datasetExists(String datasetName) {
        try {
            Dataset dataset = bigquery.getDataset(DatasetId.of(datasetName));
            if (dataset != null) {
                System.out.println("Dataset already exists.");
            } else {
                System.out.println("Dataset not found.");
            }
        } catch (BigQueryException e) {
            System.out.println("Something went wrong. \n" + e.toString());
        }
    }

    public static void createDataset(String datasetName) {
        try {
            DatasetInfo datasetInfo = DatasetInfo.newBuilder(datasetName).build();
            Dataset newDataset = bigquery.create(datasetInfo);
            String newDatasetName = newDataset.getDatasetId().getDataset();
            System.out.println(newDatasetName + " created successfully");
        } catch (BigQueryException e) {
            System.out.println("Dataset was not created. \n" + e.toString());
        }
    }

    public static void createTable(String datasetName, String tableName, Schema schema) {
        try {
            TableId tableId = TableId.of(datasetName, tableName);
            TableDefinition tableDefinition = StandardTableDefinition.of(schema);
            TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
            bigquery.create(tableInfo);
            System.out.println("Table created successfully");
        } catch (BigQueryException e) {
            System.out.println("Table was not created. \n" + e.toString());
        }
    }

    public static void deleteDataset(String projectId, String datasetName) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, datasetName);
            boolean success = bigquery.delete(datasetId, BigQuery.DatasetDeleteOption.deleteContents());
            if (success) {
                System.out.println("Dataset deleted successfully");
            } else {
                System.out.println("Dataset was not found");
            }
        } catch (BigQueryException e) {
            System.out.println("Dataset was not deleted. \n" + e.toString());
        }
    }

    public static void deleteTable(String datasetName, String tableName) {
        try {
            boolean success = bigquery.delete(TableId.of(datasetName, tableName));
            if (success) {
                System.out.println("Table deleted successfully");
            } else {
                System.out.println("Table was not found");
            }
        } catch (BigQueryException e) {
            System.out.println("Table was not deleted. \n" + e.toString());
        }
    }

    public static void getDatasetInfo(String projectId, String datasetName) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, datasetName);
            Dataset dataset = bigquery.getDataset(datasetId);
            // View dataset properties
            String description = dataset.getDescription();
            System.out.println(description);
            Page<Table> tables = bigquery.listTables(datasetName, BigQuery.TableListOption.pageSize(100));
            tables.iterateAll().forEach(table -> System.out.print(table.getTableId().getTable() + "\n"));
            System.out.println("Dataset info retrieved successfully.");
        } catch (BigQueryException e) {
            System.out.println("Dataset info not retrieved. \n" + e.toString());
        }
    }

    public static void getTableProperties(String projectId, String datasetName, String tableName) {
        try {
            TableId tableId = TableId.of(projectId, datasetName, tableName);
            Table table = bigquery.getTable(tableId);
            System.out.println("Table info: " + table.getDescription());
        } catch (BigQueryException e) {
            System.out.println("Table not retrieved. \n" + e.toString());
        }
    }

    public static void queryTotalRows(String query) {
        try {
            TableResult results = bigquery.query(QueryJobConfiguration.of(query));
            System.out.println("\n Query total rows performed successfully : " + results.getTotalRows());
        } catch (BigQueryException | InterruptedException e) {
            System.out.println("\n Query not performed \n" + e.toString());
        }
    }

    public static void listTables(String projectId, String datasetName) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, datasetName);
            Page<Table> tables = bigquery.listTables(datasetId, BigQuery.TableListOption.pageSize(100));
            tables.iterateAll().forEach(table -> System.out.print(table.getTableId().getTable() + "\n"));
            System.out.println("Tables listed successfully.");
        } catch (BigQueryException e) {
            System.out.println("Tables were not listed. Error occurred: " + e.toString());
        }
    }

    public static void queryPartitionedTable(String query,String start_date, String end_date) {
        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).addNamedParameter("start_date", QueryParameterValue.date(start_date)).addNamedParameter("end_date", QueryParameterValue.date(end_date)).build();
            TableResult results = bigquery.query(queryConfig);
            results.iterateAll().forEach(row -> row.forEach(val -> System.out.printf("%s,", val.toString())));
            System.out.println("Query partitioned table performed successfully.");
        } catch (BigQueryException | InterruptedException e) {
            System.out.println("Query not performed \n" + e.toString());
        }
    }


}
package com.cloud.accelerator.commons;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.api.gax.rpc.ServerStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BigTableUtils {


    public static void createTable(String projectId, String instanceId,String tableId, String column_family) throws IOException {
        final BigtableTableAdminClient adminClient;

        BigtableTableAdminSettings adminSettings = BigtableTableAdminSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
        // Creates a bigtable table admin client.
        adminClient = BigtableTableAdminClient.create(adminSettings);
        // Checks if table exists, creates table if does not exist.
        if (!adminClient.exists(tableId)) {
            System.out.println("Creating table: " + tableId);
            CreateTableRequest createTableRequest =
                    CreateTableRequest.of(tableId).addFamily(column_family);
            adminClient.createTable(createTableRequest);
            System.out.printf("Table %s created successfully%n", tableId);
        }}

    public static void writeToTable(String projectId, String instanceId,String tableId) throws IOException {
        final BigtableDataClient dataClient;
        final String COLUMN_FAMILY = "SAMPLE_COLUMN_FAMILY";
        final String COLUMN_QUALIFIER_GREETING = "SAMPLE_COLUMN_QUALIFIER_GREETING";
        final String COLUMN_QUALIFIER_NAME = "SAMPLE_COLUMN_QUALIFIER_NAME";
        final String ROW_KEY_PREFIX = "SAMPLE_ROW_KEY_PREFIX";
        BigtableDataSettings settings = BigtableDataSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
        dataClient = BigtableDataClient.create(settings);
        try {
            String[] names = {"value_1", "value_2", "value_3"};
            for (int i = 0; i < names.length; i++) {
                String greeting = "Hello " + names[i] + "!";
                RowMutation rowMutation = RowMutation.create(tableId, ROW_KEY_PREFIX + i)
                                .setCell(COLUMN_FAMILY, COLUMN_QUALIFIER_NAME, names[i])
                                .setCell(COLUMN_FAMILY, COLUMN_QUALIFIER_GREETING, greeting);
                dataClient.mutateRow(rowMutation);
                System.out.println(greeting);
            }
        } catch (NotFoundException e) {
            System.err.println("Failed to write to non-existent table: " + e.getMessage());
        }
    }


    public static List<Row> readTable(String projectId, String instanceId, String tableId) throws IOException {
        final BigtableDataClient dataClient;
        BigtableDataSettings settings = BigtableDataSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
        // Creates a bigtable data client.
        dataClient = BigtableDataClient.create(settings);
        try {
            System.out.println("\nReading the entire table");
            Query query = Query.create(tableId);
            ServerStream<Row> rowStream = dataClient.readRows(query);
            List<Row> tableRows = new ArrayList<>();
            for (Row r : rowStream) {
                System.out.println("Row Key: " + r.getKey().toStringUtf8());
                tableRows.add(r);
                for (RowCell cell : r.getCells()) {
                    System.out.printf(
                            "Family: %s    Qualifier: %s    Value: %s%n",
                            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
                }
            }
            return tableRows;
        } catch (NotFoundException e) {
            System.err.println("Failed to read a non-existent table: " + e.getMessage());
            return null;
        }
    }


}

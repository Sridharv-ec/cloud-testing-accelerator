package com.cloud.accelerator.commons;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.spanner.*;
import com.google.spanner.admin.database.v1.CreateDatabaseMetadata;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class CloudSpannerUtils {

    public static DatabaseClient createSpannerDBClient(String client) throws IOException {
        String PROJECT_ID = GenericUtils.readProps("projectId");
        String SPANNER_INSTANCE_ID = GenericUtils.readProps("spanner_instanceId");
        String SPANNER_DB_ID = GenericUtils.readProps("spanner_dbId");
        SpannerOptions options = SpannerOptions.newBuilder().build();
        Spanner spanner = options.getService();
        DatabaseId db = DatabaseId.of(PROJECT_ID, SPANNER_INSTANCE_ID, SPANNER_DB_ID);
        String clientProject = spanner.getOptions().getProjectId();
        if (!db.getInstanceId().getProject().equals(clientProject)) {
            System.err.println("Invalid project specified " + clientProject);
        }
        if (client.contains("dbClient")) {
            DatabaseClient dbClient = spanner.getDatabaseClient(db);
            return dbClient;
        } else {
            DatabaseAdminClient dbAdminClient = spanner.getDatabaseAdminClient();
            return (DatabaseClient) dbAdminClient;
        }
    }


    public static void createDatabase(DatabaseId id, String sql) throws IOException {
        DatabaseAdminClient dbAdminClient = (DatabaseAdminClient) createSpannerDBClient("dbAdminClient");
        OperationFuture<Database, CreateDatabaseMetadata> op =
                dbAdminClient.createDatabase(id.getInstanceId().getInstance(), id.getDatabase(), Arrays.asList(sql));
        try {
            Database db1 = op.get();
            System.out.println("Created database :" + db1.getId() + "]");
        } catch (ExecutionException e) {
            throw (SpannerException) e.getCause();
        } catch (InterruptedException e) {
            throw SpannerExceptionFactory.propagateInterrupt(e);
        }
    }


    public static void queryTable(String sql, String fieldName) throws IOException {
        DatabaseClient dbClient = createSpannerDBClient("dbClient");
        try (ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of(sql))) {
            while (resultSet.next()) {
                System.out.printf(
                        "%s %s %s\n",
                        resultSet.getLong(fieldName));
            }
        }
    }

    public static void insertUsingDml(String sql) throws IOException {
        DatabaseClient dbClient = createSpannerDBClient("dbClient");
        dbClient
                .readWriteTransaction()
                .run(transaction -> {
                    long rowCount = transaction.executeUpdate(Statement.of(sql));
                    System.out.printf("%d record inserted.\n", rowCount);
                    return null;
                });
    }

    public static void updateUsingDml(String sql) throws IOException {
        DatabaseClient dbClient = createSpannerDBClient("dbClient");
        ;
        dbClient
                .readWriteTransaction()
                .run(transaction -> {
                    long rowCount = transaction.executeUpdate(Statement.of(sql));
                    System.out.printf("%d record updated.\n", rowCount);
                    return null;
                });
    }

    public static void deleteUsingDml(String sql) throws IOException {
        DatabaseClient dbClient = createSpannerDBClient("dbClient");
        dbClient
                .readWriteTransaction()
                .run(transaction -> {
                    long rowCount = transaction.executeUpdate(Statement.of(sql));
                    System.out.printf("%d record deleted.\n", rowCount);
                    return null;
                });
    }

}
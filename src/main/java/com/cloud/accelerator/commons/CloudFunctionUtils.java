package com.cloud.accelerator.commons;

import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.cloud.pubsub.v1.SchemaServiceClient;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.Schema;
import com.google.pubsub.v1.SchemaName;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CloudFunctionUtils {

    public static void createProtoSchema(String projectId, String schemaId, String protoFile)
            throws IOException {

        ProjectName projectName = ProjectName.of(projectId);
        SchemaName schemaName = SchemaName.of(projectId, schemaId);

        // Read a proto file as a string.
        String protoSource = new String(Files.readAllBytes(Paths.get(protoFile)));

        try (SchemaServiceClient schemaServiceClient = SchemaServiceClient.create()) {

            Schema schema =
                    schemaServiceClient.createSchema(
                            projectName,
                            Schema.newBuilder()
                                    .setName(schemaName.toString())
                                    .setType(Schema.Type.PROTOCOL_BUFFER)
                                    .setDefinition(protoSource)
                                    .build(),
                            schemaId);

            System.out.println("Created a schema using a protobuf schema:\n" + schema);
        } catch (AlreadyExistsException e) {
            System.out.println(schemaName + "already exists.");
        }
    }


}

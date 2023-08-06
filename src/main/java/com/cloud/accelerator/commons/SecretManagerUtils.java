package com.cloud.accelerator.commons;

import com.google.cloud.secretmanager.v1.Secret;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretName;
import com.google.cloud.secretmanager.v1.ProjectName;
import com.google.cloud.secretmanager.v1.Replication;
import com.google.iam.v1.Binding;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient.ListSecretsPagedResponse;
import com.google.iam.v1.Policy;
import com.google.iam.v1.SetIamPolicyRequest;
import java.io.IOException;

public class SecretManagerUtils {


    public static void getSecret(String projectId, String secretId) throws IOException {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretName secretName = SecretName.of(projectId, secretId);
            Secret secret = client.getSecret(secretName);
            String replication = "";
            if (secret.getReplication().getAutomatic() != null) {
                replication = "AUTOMATIC";
            } else if (secret.getReplication().getUserManaged() != null) {
                replication = "MANAGED";
            } else {
                throw new IllegalStateException("Unknown replication type");
            }
            System.out.printf("Secret %s, replication %s\n", secret.getName(), replication);
        }
    }

    public static void createSecret(String projectId, String secretId) throws IOException {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            ProjectName projectName = ProjectName.of(projectId);
            Secret secret = Secret.newBuilder().setReplication(Replication.newBuilder()
                                            .setAutomatic(Replication.Automatic.newBuilder().build()).build()).build();
            Secret createdSecret = client.createSecret(projectName, secretId, secret);
            System.out.printf("Created secret %s\n", createdSecret.getName());
        }
    }

    public static void iamGrantAccess(String projectId, String secretId, String member)
            throws IOException {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretName secretName = SecretName.of(projectId, secretId);
            Policy currentPolicy = client.getIamPolicy(GetIamPolicyRequest.newBuilder().setResource(secretName.toString()).build());
            Binding binding = Binding.newBuilder()
                            .setRole("roles/secretmanager.secretAccessor")
                            .addMembers(member)
                            .build();
            Policy newPolicy = Policy.newBuilder().mergeFrom(currentPolicy).addBindings(binding).build();
            client.setIamPolicy(SetIamPolicyRequest.newBuilder()
                            .setResource(secretName.toString())
                            .setPolicy(newPolicy)
                            .build());
            System.out.printf("Updated IAM policy for %s\n", secretId);
        }
    }

    public static void listSecrets(String projectId) throws IOException {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            ProjectName projectName = ProjectName.of(projectId);
            ListSecretsPagedResponse pagedResponse = client.listSecrets(projectName);
            pagedResponse.iterateAll().forEach(secret -> { System.out.printf("Secret %s\n", secret.getName());
            });
        }
    }

}

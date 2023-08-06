package com.cloud.accelerator.commons;

import com.google.cloud.monitoring.v3.AlertPolicyServiceClient;
import com.google.cloud.monitoring.v3.AlertPolicyServiceClient.ListAlertPoliciesPagedResponse;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.monitoring.v3.AlertPolicy;
import com.google.monitoring.v3.ListAlertPoliciesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.protobuf.BoolValue;
import com.google.protobuf.FieldMask;
import java.io.IOException;
import java.util.List;

public class CloudMonitoringUtils {

    static Gson gson = new Gson();

    public static void listAlertPolicies(String projectId) throws IOException {
        try (AlertPolicyServiceClient client = AlertPolicyServiceClient.create()) {
            ListAlertPoliciesPagedResponse response = client.listAlertPolicies(ProjectName.of(projectId));

            System.out.println("Alert Policies:");
            for (AlertPolicy policy : response.iterateAll()) {
                System.out.println(
                        String.format("\nPolicy %s\nalert-id: %s", policy.getDisplayName(), policy.getName()));
                int channels = policy.getNotificationChannelsCount();
                if (channels > 0) {
                    System.out.println("notification-channels:");
                    for (int i = 0; i < channels; i++) {
                        System.out.println("\t" + policy.getNotificationChannels(i));
                    }
                }
                if (policy.hasDocumentation() && policy.getDocumentation().getContent() != null) {
                    System.out.println(policy.getDocumentation().getContent());
                }
            }
        }
    }

    public static List<AlertPolicy> getAlertPolicies(String projectId) throws IOException {
        List<AlertPolicy> alertPolicies = Lists.newArrayList();
        try (AlertPolicyServiceClient client = AlertPolicyServiceClient.create()) {
            ListAlertPoliciesPagedResponse response = client.listAlertPolicies(ProjectName.of(projectId));

            for (AlertPolicy policy : response.iterateAll()) {
                alertPolicies.add(policy);
            }
        }
        return alertPolicies;
    }


    public static void enablePolicies(String projectId, String filter, boolean enable)
            throws IOException {
        try (AlertPolicyServiceClient client = AlertPolicyServiceClient.create()) {
            ListAlertPoliciesPagedResponse response =
                    client.listAlertPolicies(
                            ListAlertPoliciesRequest.newBuilder()
                                    .setName(ProjectName.of(projectId).toString())
                                    .setFilter(filter)
                                    .build());

            for (AlertPolicy policy : response.iterateAll()) {
                if (policy.getEnabled().getValue() == enable) {
                    System.out.println(
                            String.format(
                                    "Policy %s is already %b.", policy.getName(), enable ? "enabled" : "disabled"));
                    continue;
                }
                AlertPolicy updatedPolicy =
                        AlertPolicy.newBuilder()
                                .setName(policy.getName())
                                .setEnabled(BoolValue.newBuilder().setValue(enable))
                                .build();
                AlertPolicy result =
                        client.updateAlertPolicy(
                                FieldMask.newBuilder().addPaths("enabled").build(), updatedPolicy);
                System.out.println(
                        String.format(
                                "%s %s",
                                result.getDisplayName(), result.getEnabled().getValue() ? "enabled" : "disabled"));
            }
        }
    }

}
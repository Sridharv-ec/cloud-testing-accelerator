package com.cloud.accelerator.commons;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.dataproc.v1.Cluster;
import com.google.cloud.dataproc.v1.ClusterConfig;
import com.google.cloud.dataproc.v1.ClusterControllerClient;
import com.google.cloud.dataproc.v1.ClusterControllerSettings;
import com.google.cloud.dataproc.v1.ClusterOperationMetadata;
import com.google.cloud.dataproc.v1.InstanceGroupConfig;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.cloud.dataproc.v1.Job;
import com.google.cloud.dataproc.v1.JobControllerClient;
import com.google.cloud.dataproc.v1.JobControllerSettings;
import com.google.cloud.dataproc.v1.JobMetadata;
import com.google.cloud.dataproc.v1.JobPlacement;
import com.google.cloud.dataproc.v1.SparkJob;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.cloud.dataproc.v1.AutoscalingConfig;
import com.google.cloud.dataproc.v1.AutoscalingPolicy;
import com.google.cloud.dataproc.v1.AutoscalingPolicyServiceClient;
import com.google.cloud.dataproc.v1.AutoscalingPolicyServiceSettings;
import com.google.cloud.dataproc.v1.BasicAutoscalingAlgorithm;
import com.google.cloud.dataproc.v1.BasicYarnAutoscalingConfig;
import com.google.cloud.dataproc.v1.InstanceGroupAutoscalingPolicyConfig;
import com.google.cloud.dataproc.v1.RegionName;
import com.google.protobuf.Duration;

public class DataProcUtils {

    public static void createClusterwithAutoscaling(
            String projectId, String region, String clusterName, String autoscalingPolicyName)
            throws IOException, InterruptedException {
        String myEndpoint = String.format("%s-dataproc.googleapis.com:443", region);

        // Configure the settings for the cluster controller client.
        ClusterControllerSettings clusterControllerSettings =
                ClusterControllerSettings.newBuilder().setEndpoint(myEndpoint).build();

        // Configure the settings for the autoscaling policy service client.
        AutoscalingPolicyServiceSettings autoscalingPolicyServiceSettings =
                AutoscalingPolicyServiceSettings.newBuilder().setEndpoint(myEndpoint).build();

        // Create a cluster controller client and an autoscaling controller client with the configured
        // settings. The clients only need to be created once and can be reused for multiple requests.
        // Using a
        // try-with-resources closes the client, but this can also be done manually with the .close()
        // method.
        try (ClusterControllerClient clusterControllerClient =
                     ClusterControllerClient.create(clusterControllerSettings);
             AutoscalingPolicyServiceClient autoscalingPolicyServiceClient =
                     AutoscalingPolicyServiceClient.create(autoscalingPolicyServiceSettings)) {

            // Create the Autoscaling policy.
            InstanceGroupAutoscalingPolicyConfig workerInstanceGroupAutoscalingPolicyConfig =
                    InstanceGroupAutoscalingPolicyConfig.newBuilder()
                            .setMinInstances(2)
                            .setMaxInstances(100)
                            .setWeight(1)
                            .build();
            InstanceGroupAutoscalingPolicyConfig secondaryWorkerInstanceGroupAutoscalingPolicyConfig =
                    InstanceGroupAutoscalingPolicyConfig.newBuilder()
                            .setMinInstances(0)
                            .setMaxInstances(100)
                            .setWeight(1)
                            .build();
            BasicYarnAutoscalingConfig basicYarnApplicationConfig =
                    BasicYarnAutoscalingConfig.newBuilder()
                            .setScaleUpFactor(0.05)
                            .setScaleDownFactor(1.0)
                            .setScaleUpMinWorkerFraction(0.0)
                            .setScaleUpMinWorkerFraction(0.0)
                            .setGracefulDecommissionTimeout(Duration.newBuilder().setSeconds(3600).build())
                            .build();
            BasicAutoscalingAlgorithm basicAutoscalingAlgorithm =
                    BasicAutoscalingAlgorithm.newBuilder()
                            .setCooldownPeriod(Duration.newBuilder().setSeconds(240).build())
                            .setYarnConfig(basicYarnApplicationConfig)
                            .build();
            AutoscalingPolicy autoscalingPolicy =
                    AutoscalingPolicy.newBuilder()
                            .setId(autoscalingPolicyName)
                            .setWorkerConfig(workerInstanceGroupAutoscalingPolicyConfig)
                            .setSecondaryWorkerConfig(secondaryWorkerInstanceGroupAutoscalingPolicyConfig)
                            .setBasicAlgorithm(basicAutoscalingAlgorithm)
                            .build();
            RegionName parent = RegionName.of(projectId, region);

            // Policy is uploaded here.
            autoscalingPolicyServiceClient.createAutoscalingPolicy(parent, autoscalingPolicy);

            // Now the policy can be referenced when creating a cluster.
            String autoscalingPolicyUri =
                    String.format(
                            "projects/%s/locations/%s/autoscalingPolicies/%s",
                            projectId, region, autoscalingPolicyName);
            AutoscalingConfig autoscalingConfig =
                    AutoscalingConfig.newBuilder().setPolicyUri(autoscalingPolicyUri).build();

            // Configure the settings for our cluster.
            InstanceGroupConfig masterConfig =
                    InstanceGroupConfig.newBuilder()
                            .setMachineTypeUri("n1-standard-2")
                            .setNumInstances(1)
                            .build();
            InstanceGroupConfig workerConfig =
                    InstanceGroupConfig.newBuilder()
                            .setMachineTypeUri("n1-standard-2")
                            .setNumInstances(2)
                            .build();
            ClusterConfig clusterConfig =
                    ClusterConfig.newBuilder()
                            .setMasterConfig(masterConfig)
                            .setWorkerConfig(workerConfig)
                            .setAutoscalingConfig(autoscalingConfig)
                            .build();

            // Create the cluster object with the desired cluster config.
            Cluster cluster =
                    Cluster.newBuilder().setClusterName(clusterName).setConfig(clusterConfig).build();

            // Create the Dataproc cluster.
            OperationFuture<Cluster, ClusterOperationMetadata> createClusterAsyncRequest =
                    clusterControllerClient.createClusterAsync(projectId, region, cluster);
            Cluster response = createClusterAsyncRequest.get();

            // Print out a success message.
            System.out.printf("Cluster created successfully: %s", response.getClusterName());

        } catch (ExecutionException e) {
            // If cluster creation does not complete successfully, print the error message.
            System.err.println(String.format("createClusterWithAutoscaling: %s ", e.getMessage()));
        }
    }


    public static void createCluster(String projectId, String region, String clusterName)
            throws IOException, InterruptedException {
        String myEndpoint = String.format("%s-dataproc.googleapis.com:443", region);

        // Configure the settings for the cluster controller client.
        ClusterControllerSettings clusterControllerSettings =
                ClusterControllerSettings.newBuilder().setEndpoint(myEndpoint).build();

        // Create a cluster controller client with the configured settings. The client only needs to be
        // created once and can be reused for multiple requests. Using a try-with-resources
        // closes the client, but this can also be done manually with the .close() method.
        try (ClusterControllerClient clusterControllerClient =
                     ClusterControllerClient.create(clusterControllerSettings)) {
            // Configure the settings for our cluster.
            InstanceGroupConfig masterConfig =
                    InstanceGroupConfig.newBuilder()
                            .setMachineTypeUri("n1-standard-2")
                            .setNumInstances(1)
                            .build();
            InstanceGroupConfig workerConfig =
                    InstanceGroupConfig.newBuilder()
                            .setMachineTypeUri("n1-standard-2")
                            .setNumInstances(2)
                            .build();
            ClusterConfig clusterConfig =
                    ClusterConfig.newBuilder()
                            .setMasterConfig(masterConfig)
                            .setWorkerConfig(workerConfig)
                            .build();
            // Create the cluster object with the desired cluster config.
            Cluster cluster =
                    Cluster.newBuilder().setClusterName(clusterName).setConfig(clusterConfig).build();

            // Create the Cloud Dataproc cluster.
            OperationFuture<Cluster, ClusterOperationMetadata> createClusterAsyncRequest =
                    clusterControllerClient.createClusterAsync(projectId, region, cluster);
            Cluster response = createClusterAsyncRequest.get();

            // Print out a success message.
            System.out.printf("Cluster created successfully: %s", response.getClusterName());

        } catch (ExecutionException e) {
            System.err.println(String.format("Error executing createCluster: %s ", e.getMessage()));
        }
    }

    public static void submitJob(
            String projectId, String region, String clusterName)
            throws IOException, InterruptedException {
        String myEndpoint = String.format("%s-dataproc.googleapis.com:443", region);

        // Configure the settings for the job controller client.
        JobControllerSettings jobControllerSettings =
                JobControllerSettings.newBuilder().setEndpoint(myEndpoint).build();

        // Create a job controller client with the configured settings. Using a try-with-resources
        // closes the client,
        // but this can also be done manually with the .close() method.
        try (JobControllerClient jobControllerClient =
                     JobControllerClient.create(jobControllerSettings)) {

            // Configure cluster placement for the job.
            JobPlacement jobPlacement = JobPlacement.newBuilder().setClusterName(clusterName).build();

            // Configure Spark job settings.
            SparkJob sparkJob =
                    SparkJob.newBuilder()
                            .setMainClass("org.apache.spark.examples.SparkPi")
                            .addJarFileUris("file:///usr/lib/spark/examples/jars/spark-examples.jar")
                            .addArgs("1000")
                            .build();

            Job job = Job.newBuilder().setPlacement(jobPlacement).setSparkJob(sparkJob).build();

            // Submit an asynchronous request to execute the job.
            OperationFuture<Job, JobMetadata> submitJobAsOperationAsyncRequest =
                    jobControllerClient.submitJobAsOperationAsync(projectId, region, job);

            Job response = submitJobAsOperationAsyncRequest.get();

            // Print output from Google Cloud Storage.
            Matcher matches =
                    Pattern.compile("gs://(.*?)/(.*)").matcher(response.getDriverOutputResourceUri());
            matches.matches();

            Storage storage = StorageOptions.getDefaultInstance().getService();
            Blob blob = storage.get(matches.group(1), String.format("%s.000000000", matches.group(2)));

            System.out.println(
                    String.format("Job finished successfully: %s", new String(blob.getContent())));

        } catch (ExecutionException e) {
            // If the job does not complete successfully, print the error message.
            System.err.println(String.format("submitJob: %s ", e.getMessage()));
        }
    }
}

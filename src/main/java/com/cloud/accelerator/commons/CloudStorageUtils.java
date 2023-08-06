package com.cloud.accelerator.commons;

import com.google.cloud.storage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CloudStorageUtils {

    public static void copyObject(
        String projectId, String sourceBucketName, String objectName, String targetBucketName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Blob blob = storage.get(sourceBucketName, objectName);
        blob.copyTo(targetBucketName);
        System.out.println("Copied object " + objectName + " from bucket " + sourceBucketName + " to " + targetBucketName);
    }


    public static void createBucket(String projectId, String bucketName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Bucket bucket = storage.create(BucketInfo.newBuilder(bucketName).build());
        System.out.println("Created bucket " + bucket.getName());
    }

    public static void deleteBucket(String projectId, String bucketName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Bucket bucket = storage.get(bucketName);
        bucket.delete();
        System.out.println("Bucket " + bucket.getName() + " was deleted");
    }

    public static void uploadObject(
        String projectId, String bucketName, String objectName, String filePath) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }


}

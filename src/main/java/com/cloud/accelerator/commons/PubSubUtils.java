package com.cloud.accelerator.commons;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.pubsub.v1.*;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.iam.v1.Policy;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;
import java.io.IOException;
import org.threeten.bp.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
//import utilities.StateProto.State;

public class PubSubUtils {

    public static void createPullSubscription(String projectId, String subscriptionId, String topicId) throws IOException {
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            TopicName topicName = TopicName.of(projectId, topicId);
            ProjectSubscriptionName subscriptionName =
                    ProjectSubscriptionName.of(projectId, subscriptionId);
            // Create a pull subscription with default acknowledgement deadline of 10 seconds.
            // Messages not successfully acknowledged within 10 seconds will get resent by the server.
            Subscription subscription =
                    subscriptionAdminClient.createSubscription(
                            subscriptionName, topicName, PushConfig.getDefaultInstance(), 10);
            System.out.println("Created pull subscription: " + subscription.getName());
        }
    }

    public static void createPushSubscription(String projectId, String subscriptionId, String topicId, String pushEndpoint) throws IOException {
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            TopicName topicName = TopicName.of(projectId, topicId);
            ProjectSubscriptionName subscriptionName =
                    ProjectSubscriptionName.of(projectId, subscriptionId);
            PushConfig pushConfig = PushConfig.newBuilder().setPushEndpoint(pushEndpoint).build();

            // Create a push subscription with default acknowledgement deadline of 10 seconds.
            // Messages not successfully acknowledged within 10 seconds will get resent by the server.
            Subscription subscription =
                    subscriptionAdminClient.createSubscription(subscriptionName, topicName, pushConfig, 10);
            System.out.println("Created push subscription: " + subscription.getName());
        }
    }

    public static void createTopic(String projectId, String topicId) throws IOException {
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            TopicName topicName = TopicName.of(projectId, topicId);
            Topic topic = topicAdminClient.createTopic(topicName);
            System.out.println("Created topic: " + topic.getName());
        }
    }

    public static void deleteSubscription(String projectId, String subscriptionId)
            throws IOException {
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            ProjectSubscriptionName subscriptionName =
                    ProjectSubscriptionName.of(projectId, subscriptionId);
            try {
                subscriptionAdminClient.deleteSubscription(subscriptionName);
                System.out.println("Deleted subscription.");
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void deleteTopic(String projectId, String topicId) throws IOException {
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            TopicName topicName = TopicName.of(projectId, topicId);
            try {
                topicAdminClient.deleteTopic(topicName);
                System.out.println("Deleted topic.");
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //When a topic admin client detaches a subscription, the subscription is no longer allowed to read any data from the topic, and all stored messages on this subscription -- unacknowledged and acknowledged -- are dropped.

    public static void detachSubscription(String projectId, String subscriptionId)
            throws IOException {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            topicAdminClient.detachSubscription(
                    DetachSubscriptionRequest.newBuilder()
                            .setSubscription(subscriptionName.toString())
                            .build());
        }

        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            Subscription subscription = subscriptionAdminClient.getSubscription(subscriptionName);
            if (subscription.getDetached()) {
                System.out.println("Subscription is detached.");
            } else {
                System.out.println("Subscription is NOT detached.");
            }
        }
    }

    //Gets the IAM policy associated with a subscription.
    public static void getSubscriptionPolicy(String projectId, String subscriptionId)
            throws IOException {
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            ProjectSubscriptionName subscriptionName =
                    ProjectSubscriptionName.of(projectId, subscriptionId);
            GetIamPolicyRequest getIamPolicyRequest =
                    GetIamPolicyRequest.newBuilder().setResource(subscriptionName.toString()).build();
            Policy policy = subscriptionAdminClient.getIamPolicy(getIamPolicyRequest);
            System.out.println("Subscription policy: " + policy);
        }
    }

//Lists subscriptions in a project.
    public static void listSubscriptionInProject(String projectId) throws IOException {
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            ProjectName projectName = ProjectName.of(projectId);
            for (Subscription subscription :
                    subscriptionAdminClient.listSubscriptions(projectName).iterateAll()) {
                System.out.println(subscription.getName());
            }
            System.out.println("Listed all the subscriptions in the project.");
        }
    }

    //Lists subscriptions in a topic.
    public static void listSubscriptionInTopic(String projectId, String topicId)
            throws IOException {
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            TopicName topicName = TopicName.of(projectId, topicId);
            for (String subscription : topicAdminClient.listTopicSubscriptions(topicName).iterateAll()) {
                System.out.println(subscription);
            }
            System.out.println("Listed all the subscriptions in the topic.");
        }
    }

    //Lists topics in a project.
    public static void listTopics(String projectId) throws IOException {
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            ProjectName projectName = ProjectName.of(projectId);
            for (Topic topic : topicAdminClient.listTopics(projectName).iterateAll()) {
                System.out.println(topic.getName());
            }
            System.out.println("Listed all topics.");
        }
    }

    //Publish messages that conform to a protocol buffer schema to a topic with a protocol buffer schema attached.

//    public static void publishProtobufMessagesExample(String projectId, String topicId)
//            throws IOException, ExecutionException, InterruptedException {
//
//        Encoding encoding = null;
//
//        TopicName topicName = TopicName.of(projectId, topicId);
//
//        // Get the topic encoding type.
//        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
//            encoding = topicAdminClient.getTopic(topicName).getSchemaSettings().getEncoding();
//        }
//
//        Publisher publisher = null;
//
//        // Instantiate a protoc-generated class defined in `us-states.proto`.
//        State state = State.newBuilder().setName("Alaska").setPostAbbr("AK").build();
//
//        block:
//        try {
//            publisher = Publisher.newBuilder(topicName).build();
//
//            PubsubMessage.Builder message = PubsubMessage.newBuilder();
//
//            // Prepare an appropriately formatted message based on topic encoding.
//            switch (encoding) {
//                case BINARY:
//                    message.setData(state.toByteString());
//                    System.out.println("Publishing a BINARY-formatted message:\n" + message);
//                    break;
//
//                case JSON:
//                    String jsonString = JsonFormat.printer().omittingInsignificantWhitespace().print(state);
//                    message.setData(ByteString.copyFromUtf8(jsonString));
//                    System.out.println("Publishing a JSON-formatted message:\n" + message);
//                    break;
//
//                default:
//                    break block;
//            }
//
//            // Publish the message.
//            ApiFuture<String> future = publisher.publish(message.build());
//            System.out.println("Published message ID: " + future.get());
//
//        } finally {
//            if (publisher != null) {
//                publisher.shutdown();
//                publisher.awaitTermination(1, TimeUnit.MINUTES);
//            }
//        }
//    }

    //Creates a publisher client with custom batching settings and uses it to publish some messages.

    public static void publishWithBatchSettings(String projectId, String topicId)
            throws IOException, ExecutionException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;
        List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

        try {
            // Batch settings control how the publisher batches messages
            long requestBytesThreshold = 5000L; // default : 1 byte
            long messageCountBatchSize = 100L; // default : 1 message

            Duration publishDelayThreshold = Duration.ofMillis(100); // default : 1 ms

            // Publish request get triggered based on request size, messages count & time since last
            // publish, whichever condition is met first.
            BatchingSettings batchingSettings =
                    BatchingSettings.newBuilder()
                            .setElementCountThreshold(messageCountBatchSize)
                            .setRequestByteThreshold(requestBytesThreshold)
                            .setDelayThreshold(publishDelayThreshold)
                            .build();

            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).setBatchingSettings(batchingSettings).build();

            // schedule publishing one message at a time : messages get automatically batched
            for (int i = 0; i < 100; i++) {
                String message = "message " + i;
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                messageIdFutures.add(messageIdFuture);
            }
        } finally {
            // Wait on any pending publish requests.
            List<String> messageIds = ApiFutures.allAsList(messageIdFutures).get();

            System.out.println("Published " + messageIds.size() + " messages with batch settings.");

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    //Uses asynchronous pull to receive messages.
    public static void subscribeAsync(String projectId, String subscriptionId) {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        // Instantiate an asynchronous message receiver.
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    // Handle incoming message, then ack the received message.
                    System.out.println("Id: " + message.getMessageId());
                    System.out.println("Data: " + message.getData().toStringUtf8());
                    consumer.ack();
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
            System.out.printf("Listening for messages on %s:\n", subscriptionName.toString());
            // Allow the subscriber to run for 30s unless an unrecoverable error occurs.
            subscriber.awaitTerminated(30, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            // Shut down the subscriber after 30s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }

    //Uses synchronous pull to receive messages.
    public static void subscribeSync(
            String projectId, String subscriptionId, Integer numOfMessages) throws IOException {
        SubscriberStubSettings subscriberStubSettings =
                SubscriberStubSettings.newBuilder()
                        .setTransportChannelProvider(
                                SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
                                        .setMaxInboundMessageSize(20 * 1024 * 1024) // 20MB (maximum message size).
                                        .build())
                        .build();

        try (SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
            String subscriptionName = ProjectSubscriptionName.format(projectId, subscriptionId);
            PullRequest pullRequest =
                    PullRequest.newBuilder()
                            .setMaxMessages(numOfMessages)
                            .setSubscription(subscriptionName)
                            .build();

            // Use pullCallable().futureCall to asynchronously perform this operation.
            PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);
            List<String> ackIds = new ArrayList<>();
            for (ReceivedMessage message : pullResponse.getReceivedMessagesList()) {
                // Handle received message
                // ...
                ackIds.add(message.getAckId());
            }
            // Acknowledge received messages.
            AcknowledgeRequest acknowledgeRequest =
                    AcknowledgeRequest.newBuilder()
                            .setSubscription(subscriptionName)
                            .addAllAckIds(ackIds)
                            .build();

            // Use acknowledgeCallable().futureCall to asynchronously perform this operation.
            subscriber.acknowledgeCallable().call(acknowledgeRequest);
            System.out.println(pullResponse.getReceivedMessagesList());
        }
    }
}

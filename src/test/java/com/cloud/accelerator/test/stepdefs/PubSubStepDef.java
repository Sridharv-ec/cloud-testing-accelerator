package com.cloud.accelerator.test.stepdefs;

import com.cloud.accelerator.commons.PubSubUtils;
import com.cloud.accelerator.commons.GenericUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class PubSubStepDef {

    String projectId = GenericUtils.readProps("projectId");

    public PubSubStepDef() throws IOException {
    }

    @Given("Create pull subscription for topic {string} with subscriptionId {string}")
    public void createPullSubscription(String topicId, String subscriptionId) throws IOException {
        PubSubUtils.createPullSubscription(projectId, subscriptionId, topicId);
    }

    @And("Create push subscription at {string} endpoint for topic {string} with subscriptionId {string}")
    public void createPushSubscription(String subscriptionId, String topicId, String pushEndpoint) throws IOException {
        PubSubUtils.createPushSubscription(projectId, subscriptionId, topicId, pushEndpoint);
    }

    @And("Create topic {string}")
    public void createTopic(String topicId) throws IOException {
        PubSubUtils.createTopic(projectId, topicId);
    }

    @And("Delete subscription with subscriptionId {string}")
    public void deleteSubscription(String subscriptionId) throws IOException {
        PubSubUtils.deleteSubscription(projectId, subscriptionId);
    }

    @And("Delete topic {string}")
    public void deleteTopic(String topicId) throws IOException {
        PubSubUtils.deleteTopic(projectId, topicId);
    }

    @And("Detach subscription with subscriptionId {string}")
    public void detachSubscription(String subscriptionId) throws IOException {
        PubSubUtils.detachSubscription(projectId, subscriptionId);
    }

    @And("Get subscription policy for subscriptionId {string}")
    public void getSubscriptionPolicy(String subscriptionId) throws IOException {
        PubSubUtils.getSubscriptionPolicy(projectId, subscriptionId);
    }

    @And("Get Subscription list of the project")
    public void listSubscriptionInProject() throws IOException {
        PubSubUtils.listSubscriptionInProject(projectId);
    }

    @And("Get Subscription list for topic {string}")
    public void listSubscriptionInTopic(String topicId) throws IOException {
        PubSubUtils.listSubscriptionInTopic(projectId, topicId);
    }

    @And("List all topics")
    public void listTopics() throws IOException {
        PubSubUtils.listTopics(projectId);
    }

    @And("CPublish with batch on topic {string}")
    public void publishWithBatchSettings(String topicId) throws IOException, ExecutionException, InterruptedException {
        PubSubUtils.publishWithBatchSettings(projectId, topicId);
    }

    @And("Subscribe async for subscriptionId {string}")
    public void subscribeAsync(String subscriptionId) {
        PubSubUtils.subscribeAsync(projectId, subscriptionId);
    }

    @And("Subscribe sync for subscriptionId {string}")
    public void subscribeSync(String subscriptionId, Integer numOfMessages) throws IOException {
        PubSubUtils.subscribeSync(projectId, subscriptionId, numOfMessages);
    }

}

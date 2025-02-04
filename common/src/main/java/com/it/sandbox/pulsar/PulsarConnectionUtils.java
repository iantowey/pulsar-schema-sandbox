package com.it.sandbox.pulsar;

//import com.google.protobuf.GeneratedMessageV3;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.client.impl.schema.StringSchema;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class PulsarConnectionUtils {
    private static final String SERVICE_URL = "pulsar://localhost:6650";

    public static PulsarClient client = null;

    static {
        try {
            client = PulsarClient.builder().serviceUrl(SERVICE_URL).build();
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Consumer<T> craeteConsumer(String topicName, Schema<T> schema, String name) throws PulsarClientException {
        return client.newConsumer(schema)
                .consumerName(name)
                .topic(topicName)
                .subscriptionName(topicName + "_sub_typed")
//                .subscriptionType(SubscriptionType.Shared)
//                .enableRetry(true)
//                .negativeAckRedeliveryDelay(1, TimeUnit.SECONDS)
//                .deadLetterPolicy(DeadLetterPolicy.builder()
////                        .retryLetterTopic(topicName + "-retry")
//                        .deadLetterTopic(topicName + "-DLQ")
//                        .maxRedeliverCount(1)
//                        .build()
//                )
                .subscribe();
    }

    public static BiFunction<String, String, Consumer<String>> STRING_CONSUMER = (topicName, consumerrName) -> {
        try {
            return craeteConsumer(topicName, Schema.STRING, consumerrName);
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    };

    public static BiFunction<String, String, Consumer<byte[]>> BYTE_ARRAY_CONSUMER = (topicName, consumerrName) -> {
        try {
            return craeteConsumer(topicName, Schema.BYTES, consumerrName);
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    };

    public static <T> Consumer<T> avroConsumer(String topicName, Class cls, String name) throws PulsarClientException {
        SchemaDefinition<T> sd = SchemaDefinition.<T>builder().withPojo(cls).withAlwaysAllowNull(false).withJSR310ConversionEnabled(false).build();
        return craeteConsumer(topicName, Schema.AVRO(sd), name);
    }

    public static <T> Consumer<T> jsonConsumer(String topicName, Class cls, String name) throws PulsarClientException {
        SchemaDefinition<T> sd = SchemaDefinition.<T>builder().withPojo(cls).withAlwaysAllowNull(false).withJSR310ConversionEnabled(false).build();
        return craeteConsumer(topicName, Schema.JSON(sd), name);
    }

    public static <T> Producer<T> createProducer(String topicName, Schema<T> schema, String name) {
        try {
            return client.newProducer(schema)
                    .producerName(name)
                    .topic(topicName)
                    .create();
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }

    public static BiFunction<String, String, Producer<String>> STRING_PRODUCER = (topicName, producerName) -> createProducer(topicName, Schema.STRING, producerName);

    public static BiFunction<String, String, Producer<byte[]>> BYTE_ARRAY_PRODUCER = (topicName, producerName) -> createProducer(topicName, Schema.BYTES, producerName);

    public static <T> Producer<T> jsonProducer(String topicName, Class<T> cls, String name) {
        SchemaDefinition<T> sd = SchemaDefinition.<T>builder().withPojo(cls).withAlwaysAllowNull(false).withJSR310ConversionEnabled(false).build();
        return createProducer(topicName,Schema.JSON(sd) , name);
    }

    public static <T> Producer<T> avroProducer(String topicName, Class<T> t, String name) {
        SchemaDefinition<T> sd = SchemaDefinition.<T>builder().withPojo(t).withAlwaysAllowNull(false).withJSR310ConversionEnabled(false).build();
        return createProducer(topicName, Schema.AVRO(sd), name);
    }


}
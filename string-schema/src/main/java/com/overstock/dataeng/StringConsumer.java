package com.it.sandbox.pulsar;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Messages;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class StringConsumer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Class cls = Class.forName(args[0]);

        Consumer<String> consumer = PulsarConnectionUtils.STRING_CONSUMER.apply(args[1], args[2]);

        while (true) {
            Thread.sleep(500);
            Messages<String> msgs = consumer.batchReceive();

            ConsumerBatchMessages<String> ack = new ConsumerBatchMessages<>();
//            ConsumerBatchMessages<String> nack = new ConsumerBatchMessages<>();
            if (msgs.size() > 0) {
                msgs.forEach(msg -> {
                    try {
                        /***
                         * This the issue with String schema topics,
                         *  - all consumers need to serialise objects to json
                         *  - all producers need to deserialize objects to json
                         * Duplicate code across all the
                         */
                        Object p = OBJECT_MAPPER.readValue(msg.getData(), cls);
                        ack.add(msg);
                        System.out.println("Received: " + msg.getValue());
                    } catch (IOException e) {
                        ack.add(msg);
//                        nack.add(msg);
                        System.out.println("ERROR: unserialisable to Product class" + msg.getValue());
                    }
                });
                consumer.acknowledge(ack);
//                consumer.negativeAcknowledge(nack);
            } else {
                System.out.println("No messages to process");
            }
        }

    }
}
package com.it.sandbox.pulsar;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Messages;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class JsonConsumer {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Class cls = Class.forName(args[0]);
        String topic = args[1];
        String name = args[2];
        System.out.println(Arrays.stream(args).collect(Collectors.joining("\n", "**************\n", "\n**************\n")));

        Consumer<Object> consumer = PulsarConnectionUtils.jsonConsumer(topic, cls, name);


        boolean receivedMsg = false;
        do {
            Thread.sleep(500);
            Messages<Object> msgs = consumer.batchReceive();

            ConsumerBatchMessages<Object> ack = new ConsumerBatchMessages<>();
            ConsumerBatchMessages<Object> nack = new ConsumerBatchMessages<>();

            if(msgs.size() > 0){
                msgs.forEach(msg -> {
                    Object p;
                    try {
                        p = msg.getValue(); // validation occurs on
                        System.out.println("Received: " + p.toString() );
                        if (new Random().nextDouble() <= 0.5) {
                            ack.add(msg);
                        } else {
                            nack.add(msg);
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + new String(msg.getData()) );
                        nack.add(msg);
                    }
                });
                consumer.acknowledge(ack);
                consumer.negativeAcknowledge(nack);
                receivedMsg = true;
            } else {
                receivedMsg = false;
            }

        } while (receivedMsg);
        consumer.close();
        PulsarConnectionUtils.client.close();
    }
}
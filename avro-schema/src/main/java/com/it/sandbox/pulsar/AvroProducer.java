package com.it.sandbox.pulsar;

import org.apache.pulsar.client.api.Producer;
import org.instancio.Instancio;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AvroProducer {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Class cls = Class.forName(args[0]);
        String topic = args[1];
        String name = args[2];
        System.out.println(Arrays.stream(args).collect(Collectors.joining("\n", "**************\n", "\n**************\n")));

        Producer<Object> producer = PulsarConnectionUtils.avroProducer(topic, cls, name);
        while(true){
            Object p = Instancio.of(cls).create();
            producer.send(p);
            System.out.println("sent: " + p.toString());
            Thread.sleep(500);
        }
    }
}
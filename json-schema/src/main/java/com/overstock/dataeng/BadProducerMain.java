package com.it.sandbox.pulsar;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class BadProducerMain {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Class cls = Class.forName(args[0]);
        String topic = args[1];
        System.out.println(Arrays.stream(args).collect(Collectors.joining("\n", "**************\n", "\n**************\n")));

        Producer<byte[]> producer = PulsarConnectionUtils.client.newProducer(Schema.AUTO_PRODUCE_BYTES()).topic(topic).create();
        while(true){
            Object p = Instancio.create(cls);
            byte[] a =  new Random().nextDouble() <= 0.5 ? "{\"id\": \"str\"}".getBytes() : OBJECT_MAPPER.writeValueAsBytes(p);
//            byte[] a =  new Random().nextDouble() <= 0.5 ? "blah blah blah".getBytes() : OBJECT_MAPPER.writeValueAsBytes(p);
            producer.send(a);
            System.out.println("sent: " + new String(a));
            Thread.sleep(500);
        }
    }
}
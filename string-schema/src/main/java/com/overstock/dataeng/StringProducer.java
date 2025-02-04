package com.it.sandbox.pulsar;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class StringProducer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(500);

        Class cls = Class.forName(args[0]);

        Producer<String> producer = PulsarConnectionUtils.STRING_PRODUCER.apply(args[1], args[2]);
        while(LocalDateTime.now().compareTo(localDateTime) == -1){
            Object p = Instancio.of(cls).create();
            String s = new Random().nextDouble() <= 0.5 ? "corrupted message" : OBJECT_MAPPER.writeValueAsString(p);
            producer.send(s);
            System.out.println("sent: " + s);
            Thread.sleep(500);
        }
        producer.close();
        PulsarConnectionUtils.client.close();
    }
}
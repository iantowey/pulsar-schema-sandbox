package com.it.sandbox.pulsar;

import com.it.sandbox.pulsar.avro.Product;
import org.apache.pulsar.client.api.Producer;
import org.instancio.Instancio;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JsonProducer {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Class cls = Class.forName(args[0]);
        String topic = args[1];
        String name = args[2];
        System.out.println(Arrays.stream(args).collect(Collectors.joining("\n", "**************\n", "\n**************\n")));

        Producer<Object> producer = PulsarConnectionUtils.jsonProducer(topic, cls, name);
        producer.send("some message");
        producer.sendAsync("some message");
        producer.newMessage().value("some message").deliverAfter(1, TimeUnit.MINUTES).send();
        producer.newMessage().value("some message").deliverAt(LocalDateTime.now().plus(1, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).send();

        while(true){
            Product p = Instancio.create(Product.class);
            producer.send(p);
            producer.newMessage();
            System.out.println("sent: " + p.toString());
            Thread.sleep(500);
        }
    }
}
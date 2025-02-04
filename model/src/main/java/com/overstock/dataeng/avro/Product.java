package com.it.sandbox.pulsar.avro;

import lombok.Data;
import lombok.ToString;
import org.apache.avro.reflect.Nullable;

import java.util.List;

@Data
@ToString
public class Product {

    private long id;
    private String name;

    @Nullable
    private List<Options> options;

    private double price;

}


package com.it.sandbox.pulsar.json;

import lombok.Data;
import lombok.ToString;
import org.apache.avro.reflect.Nullable;

import java.util.List;

@Data
@ToString
public class Product {

    private long id;

    @Nullable
    private String name;

    @Nullable
    private List<Options> options;

    @Nullable
    private double price;

}


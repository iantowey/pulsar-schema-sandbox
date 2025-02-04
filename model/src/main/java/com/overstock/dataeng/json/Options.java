package com.it.sandbox.pulsar.json;

import lombok.Data;
import org.apache.avro.reflect.Nullable;

@Data
public class Options {
    private long id;
    @Nullable
    private String name;
}

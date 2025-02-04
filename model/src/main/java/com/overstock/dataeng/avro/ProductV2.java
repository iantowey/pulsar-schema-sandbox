package com.it.sandbox.pulsar.avro;

import lombok.Data;
import org.apache.avro.reflect.Nullable;

@Data
public class ProductV2 extends Product  {

    @Nullable
    private String someOptionalField;

}


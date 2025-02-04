package com.it.sandbox.pulsar.avro;

import lombok.Data;

@Data
public class ProductV3 extends Product  {
    private String someMandatoryField;
}


#### Pulsar "String schema" messages 

Schema of type "String" are currently the type DataEng has mandated client apps to use.

```commandline
sdk use java 11.0.2-open
mvn clean compile install
```

#### Produce
```commandline
mvn exec:java -pl string-schema -Dexec.mainClass=com.it.sandbox.pulsar.StringProducer -Dexec.args="com.it.sandbox.pulsar.avro.Product product-obj-as-string-schema my-string-producing-app"
```

#### Consume
```commandline
mvn exec:java -pl string-schema -Dexec.mainClass=com.it.sandbox.pulsar.StringConsumer -Dexec.args="com.it.sandbox.pulsar.avro.Product product-obj-as-string-schema my-string-consuming-app"
```


Get the schema definition from the registry, when the producer starts producing pulsar auto register a string schema to the topics.
```commandline
docker exec pulsar bin/pulsar-admin schemas get product-obj-as-string-schema

{
  "version": 0,
  "schemaInfo": {
    "name": "product-obj-as-string-schema",
    "schema": "",
    "type": "STRING",
    "timestamp": 1679263405504,
    "properties": {}
  }
}


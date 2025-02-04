#### Users upload a schema, all schema in pul;sar are avro format, but the schema type can be JSON/AVRO/PROTOBUFS/..

https://docs.spring.io/spring-pulsar/docs/current-SNAPSHOT/reference/html/

```commandline
sdk use java 11.0.2-open
mvn clean compile install
```

#### create a topic
```commandline
docker exec pulsar bin/pulsar-admin topics delete product-obj-as-json-schema
docker exec pulsar bin/pulsar-admin topics create product-obj-as-json-schema
docker exec pulsar bin/pulsar-admin topics stats product-obj-as-json-schema
docker exec pulsar bin/pulsar-admin topics info-internal product-obj-as-json-schema
```


#### upload schema to registry
```commandline
docker cp json-schema/src/main/resources/product-schema.json pulsar:/tmp
docker exec pulsar bin/pulsar-admin schemas upload product-obj-as-json-schema -f /tmp/product-schema.json
```

#### View the schema definition from the registry, auto created topics for DLQ and retry will ahve the same schema registered
```commandline
docker exec pulsar bin/pulsar-admin schemas get product-obj-as-json-schema
docker exec pulsar bin/pulsar-admin schemas get product-obj-as-json-schema-DLQ
docker exec pulsar bin/pulsar-admin schemas get product-obj-as-json-schema-retry
```


#### Run producers/consumers
```commandline
mvn exec:java -pl json-schema -Dexec.mainClass=com.it.sandbox.pulsar.JsonProducer -Dexec.args="com.it.sandbox.pulsar.json.Product product-obj-as-json-schema my-json-producing-app"
mvn exec:java -pl json-schema -Dexec.mainClass=com.it.sandbox.pulsar.JsonConsumer -Dexec.args="com.it.sandbox.pulsar.json.Product product-obj-as-json-schema my-json-consuming-app"
```

#### Bad producer

there is a way to get invalid json to the topic, by creating a producer of type byte[] that 
can send send valid json but invalid for the registered schema, validation only occurs on the client side 
on object serialisation.

```commandline
docker exec pulsar bin/pulsar-admin namespaces get-is-allow-auto-update-schema public/default
docker exec pulsar bin/pulsar-admin namespaces get-schema-validation-enforce public/default

mvn exec:java -pl json-schema -Dexec.mainClass=com.it.sandbox.pulsar.BadProducerMain -Dexec.args="com.it.sandbox.pulsar.json.Product product-obj-as-json-schema my-json-producing-app"
```


#### delete schema
```commandline
docker exec pulsar bin/pulsar-admin schemas delete product-obj-as-json-schema -f
```


#### view topics
```commandline
docker exec pulsar bin/pulsar-admin topics list public/default | sort

```

# Misc
```commandline
ds exec pulsar bin/pulsar-admin topics stats product-obj-as-json-schema | jq .

ds exec pulsar bin/pulsar-admin topics stats product-obj-as-json-schema-DLQ | jq .

ds exec pulsar bin/pulsar-admin topics stats product-obj-as-json-schema | jq '{
msgInCounter:.msgInCounter,
msgOutCounter:.msgOutCounter,
sub_backlogSize:.subscriptions."product-json-schema-sub".msgBacklog,
sub_msgBacklogNoDelayed:.subscriptions."product-json-schema-sub".msgBacklogNoDelayed
}'

ds exec pulsar bin/pulsar-admin topics stats product-obj-as-json-schema-DLQ | jq '{
msgInCounter:.msgInCounter,
msgOutCounter:.msgOutCounter,
sub_backlogSize:.subscriptions."product-json-schema-sub".msgBacklog,
sub_msgBacklogNoDelayed:.subscriptions."product-json-schema-sub".msgBacklogNoDelayed
}'

ds exec pulsar bin/pulsar-admin topics stats product-obj-as-json-schema-retry | jq '{
msgInCounter:.msgInCounter,
msgOutCounter:.msgOutCounter,
sub_backlogSize:.subscriptions."product-json-schema-sub".msgBacklog,
sub_msgBacklogNoDelayed:.subscriptions."product-json-schema-sub".msgBacklogNoDelayed
}'

```
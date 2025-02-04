#### Setup
```commandline
sdk use java 11.0.2-open
mvn clean compile package -DskipTests=true -Ddocker.skip=true#
```

#### Create topic 
```commandline
docker exec pulsar bin/pulsar-admin topics delete product-avro-schema
docker exec pulsar bin/pulsar-admin topics create product-avro-schema
docker exec pulsar bin/pulsar-admin topics stats product-avro-schema
docker exec pulsar bin/pulsar-admin topics info-internal product-avro-schema
```

#### By default auto schema update is enabled, we will want to disable this; if this is enabled and depending on schema compability strategy consumer/producers could register schemas on inital connection
```commandline
docker exec pulsar bin/pulsar-admin namespaces set-is-allow-auto-update-schema --disable public/default
#docker exec pulsar bin/pulsar-admin namespaces set-is-allow-auto-update-schema --enable public/default
```
#### By default schema , schema validation is not enforced on producers, we will want to enable it 
```commandline
docker exec pulsar bin/pulsar-admin namespaces set-schema-validation-enforce --enable public/default
#docker exec pulsar bin/pulsar-admin namespaces set-schema-validation-enforce --disable public/default
#docker exec pulsar bin/pulsar-admin namespaces get-is-allow-auto-update-schema public/default
#docker exec pulsar bin/pulsar-admin namespaces get-schema-validation-enforce public/default
```


#### Register schema using java Bean, use flag `--always-allow-null` to make all fields mandatory unless annotated with `@org.apache.avro.reflect.Nullable`
```commandline
docker cp model/target/model-1.0-SNAPSHOT.jar pulsar:/tmp
docker exec pulsar bin/pulsar-admin schemas extract product-avro-schema --classname com.it.sandbox.pulsar.avro.Product --jar /tmp/model-1.0-SNAPSHOT.jar --type avro --always-allow-null false
docker exec pulsar bin/pulsar-admin schemas get product-avro-schema 
docker exec pulsar bin/pulsar-admin schemas get product-avro-schema -a
```

#### Set update strategy
```commandline
# This schema comp strategy does not appear to work for my config - ned to investigate further
#docker exec -it pulsar bin/pulsar-admin topicPolicies set-schema-compatibility-strategy -s ALWAYS_INCOMPATIBLE product-avro-schema 

docker exec -it pulsar bin/pulsar-admin topicPolicies set-schema-compatibility-strategy -s FULL product-avro-schema 
docker exec -it pulsar bin/pulsar-admin topicPolicies get-schema-compatibility-strategy product-avro-schema 
```

#### producers/consumer
```commandline
cd ./pulsar-schema-sandbox
sdk use java 11.0.2-open
mvn clean compile package -DskipTests=true -Ddocker.skip=true

mvn compile exec:java \
-pl avro-schema \
-Dexec.mainClass=com.it.sandbox.pulsar.AvroProducer \
-Dexec.args="com.it.sandbox.pulsar.avro.Product product-avro-schema my-avro-producer"

#This cmd throws a IncompatibleSchemaException - unknown why, didnt look into 
mvn compile exec:java \
-pl avro-schema \
-Dexec.mainClass=com.it.sandbox.pulsar.AvroConsumer \
-Dexec.args="com.it.sandbox.pulsar.avro.Product product-avro-schema my-avro-consumer"

java -cp ./avro-schema/target/avro-schema-1.0-SNAPSHOT-jar-with-dependencies.jar \
com.it.sandbox.pulsar.AvroProducer \
"com.it.sandbox.pulsar.avro.Product" "product-avro-schema" "my-avro-producer-cli"

java -cp ./avro-schema/target/avro-schema-1.0-SNAPSHOT-jar-with-dependencies.jar \
com.it.sandbox.pulsar.AvroConsumer \
"com.it.sandbox.pulsar.avro.Product" "product-avro-schema" "my-avro-consumer"

```
https://github.com/apache/pulsar/issues/9571

https://pulsar.apache.org/docs/2.11.x/admin-api-schemas/#set-schema-compatibility-check-strategy

https://pulsar.apache.org/docs/2.11.x/schema-understand/#schema-compatibility-check-strategy

#### Compile project
```commandline
# use java 11
cd ./pulsar-schema-sandbox
sdk use java 11.0.2-open
mvn clean compile package
```

#### Create topic
```commandline
docker exec pulsar bin/pulsar-admin topics delete product-avro-schema
docker exec pulsar bin/pulsar-admin topics create product-avro-schema
docker exec pulsar bin/pulsar-admin topics stats product-avro-schema
docker exec pulsar bin/pulsar-admin topics info-internal product-avro-schema
```

#### Disable auto create topics
```commandline
docker exec pulsar bin/pulsar-admin namespaces set-is-allow-auto-update-schema --disable public/default
```

#### Set schema-compatibility-strategy
```commandline
docker exec pulsar bin/pulsar-admin namespaces set-schema-compatibility-strategy public/default -c ALWAYS_INCOMPATIBLE
docker exec pulsar bin/pulsar-admin namespaces get-schema-compatibility-strategy publ#c/default
docker exec pulsar bin/pulsar-admin namespaces policies public/default

docker exec pulsar bin/pulsar-admin namespaces get-is-allow-auto-update-schema public/default
docker exec pulsar bin/pulsar-admin namespaces set-is-allow-auto-update-schema --disable public/default
docker exec pulsar bin/pulsar-admin namespaces set-schema-validation-enforce --enable public/default
docker exec pulsar bin/pulsar-admin namespaces set-schema-validation-enforce --disable public/default
docker exec pulsar bin/pulsar-admin namespaces get-schema-validation-enforce public/default
docker exec pulsar bin/pulsar-admin bin/pulsar-admin namespaces policies public/default


```

#### Register Schema
```commandline
docker exec pulsar bin/pulsar-admin topics set-schema-validation-enforce --enable product-avro-schema
docker exec pulsar bin/pulsar-admin topicPolicies set-inactive-topic-policies --disable-delete-while-inactive product-avro-schema
docker cp ./pulsar-schema-sandbox/model/target/model-1.0-SNAPSHOT.jar pulsar:/tmp
docker exec pulsar bin/pulsar-admin schemas extract product-avro-schema --classname com.it.sandbox.pulsar.V1Product --jar /tmp/model-1.0-SNAPSHOT.jar --type avro
docker exec pulsar bin/pulsar-admin schemas get product-avro-schema 
docker exec pulsar bin/pulsar-admin schemas delete product-avro-schema 
docker exec pulsar bin/pulsar-admin topicPolicies set-schema-compatibility-strategy -s ALWAYS_INCOMPATIBLE product-avro-schema
docker exec pulsar bin/pulsar-admin topicPolicies set-schema-compatibility-strategy -s BACKWARD product-avro-schema
docker exec pulsar bin/pulsar-admin topicPolicies get-schema-compatibility-strategy product-avro-schema 
```

#### Start Producer
```commandline
cd ./pulsar-schema-sandbox
sdk use java 11.0.2-open
mvn clean compile package
mvn exec:java -pl avro-schema -Dexec.mainClass=com.it.sandbox.pulsar.ProducerMain -Dexec.args="com.it.sandbox.pulsar.V1Product product-avro-schema"
```

```commandline

pulsar/bin/pulsar-admin topics create

docker cp ./pulsar-schema-sandbox/model/target/model-1.0-SNAPSHOT.jar nginx:/usr/share/nginx/html

docker exec pulsar bin/pulsar-admin topics create product-avro-schema
docker exec pulsar bin/pulsar-admin topics get product-avro-schema

docker exec pulsar bin/pulsar-admin schemas delete product-avro-schema -f

docker exec pulsar bin/pulsar-admin namespaces set-schema-validation-enforce --enable public/default

docker exec -it pulsar bin/pulsar-admin topicPolicies set-schema-compatibility-strategy -s ALWAYS_INCOMPATIBLE product-avro-schema
docker exec -it pulsar bin/pulsar-admin topicPolicies get-schema-compatibility-strategy product-avro-schema 

docker exec -it pulsar ./bin/pulsar-admin namespaces get-is-allow-auto-update-schema public/default 
docker exec -it pulsar ./bin/pulsar-admin namespaces set-is-allow-auto-update-schema public/default --disable


docker cp ./pulsar-schema-sandbox/avro-schema/src/main/resources/produce-schema-v1.json pulsar:/tmp
docker cp ./pulsar-schema-sandbox/avro-schema/src/main/resources/produce-schema-v2.json pulsar:/tmp
docker cp ./pulsar-schema-sandbox/avro-schema/src/main/resources/produce-schema-v3.json pulsar:/tmp

docker exec pulsar bin/pulsar-admin schemas upload product-avro-schema -f /tmp/produce-schema-v1.json
docker exec pulsar bin/pulsar-admin schemas upload product-avro-schema -f /tmp/produce-schema-v2.json
docker exec pulsar bin/pulsar-admin schemas upload product-avro-schema -f /tmp/produce-schema-v3.json

#use class to register schema
docker cp ./pulsar-schema-sandbox/model/target/model-1.0-SNAPSHOT.jar pulsar:/tmp
docker exec pulsar bin/pulsar-admin schemas extract product-avro-schema --classname com.it.sandbox.pulsar.V1Product --jar /tmp/model-1.0-SNAPSHOT.jar --type avro
docker exec pulsar bin/pulsar-admin schemas extract product-avro-schema --classname com.it.sandbox.pulsar.V2Product --jar /tmp/model-1.0-SNAPSHOT.jar --type avro
docker exec pulsar bin/pulsar-admin schemas extract product-avro-schema --classname com.it.sandbox.pulsar.V3Product --jar /tmp/model-1.0-SNAPSHOT.jar --type avro
docker exec pulsar bin/pulsar-admin schemas get product-avro-schema 
docker exec pulsar bin/pulsar-admin topics stats product-avro-schema
docker exec pulsar bin/pulsar-admin topics info-internal product-avro-schema
docker exec -it pulsar bin/pulsar-client consume -n 0 -s sb product-avro-schema

```
cd ./pulsar-schema-sandbox
mvn exec:java -pl avro-schema -Dexec.mainClass=com.it.sandbox.pulsar.V1ProducerMain

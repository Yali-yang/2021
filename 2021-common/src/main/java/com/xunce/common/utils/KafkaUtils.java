package com.xunce.common.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaUtils {
    public static final String krb5Conf = "C:\\Users\\admin\\Desktop\\bug\\17kbs\\krb5.conf";
    public static final String kafkaJaasConf = "C:\\Users\\admin\\Desktop\\bug\\17kbs\\kafka_server_jaas.conf";

    public static final String bootstrapServers = "cmf-test-tdh-01:9092";

    public static final String topic = "topic1";

    public static void main(String[] args) {
        System.setProperty("java.security.krb5.conf", krb5Conf);
        System.setProperty("java.security.auth.login.config", kafkaJaasConf);
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //Kerberos认证必须添加以下三行
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");
        KafkaProducer producer = new KafkaProducer(props);
        String message = "Hello world";
        System.out.println(message);
        producer.send(new ProducerRecord(topic, message), (data, exception) -> {
            if (null != exception) {
                System.out.println("发送消息至Kafka失败!!!");
            } else {
                System.out.println("发送消息至Kafka成功!!!");
            }
        });
        producer.close();

        maifn(null);
    }


    public static final String comsumerGroup = "group_topic1";

    public static void maifn(String[] args) {
        System.setProperty("java.security.krb5.conf", krb5Conf);
        System.setProperty("java.security.auth.login.config", kafkaJaasConf);
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", comsumerGroup);
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "GSSAPI");
        props.put("sasl.kerberos.service.name", "kafka");
        KafkaConsumer kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        while(true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records)
                System.out.println("Partition: " + record.partition() + " Offset: " + record.offset() + " Value: " + record.value() + " ThreadID: " + Thread.currentThread().getId());
            kafkaConsumer.close();
        }
    }
}

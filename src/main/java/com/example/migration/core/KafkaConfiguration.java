package com.example.migration.core;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;
import com.nutmeg.user.service.CustomerEmailEventKey;
import com.nutmeg.user.service.UserOrganisationEvent;
import com.nutmeg.user.service.CustomerEmailEventValue;
import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.PARTITIONER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Slf4j
@Configuration
public class KafkaConfiguration {

    @Bean
    Producer<String, UserOrganisationEvent> userOrganisationKafkaProducer(
        @Value("${kafka.bootstrap.servers}") String bootstrapServers,
        @Value("${schema.registry.url}") String schemaRegistryUrl
    ) {
        return new KafkaProducer<>(buildPropertiesKeyStringSerializer("reprocess-user-organization", bootstrapServers, schemaRegistryUrl));
    }

    @Bean
    Producer<CustomerEmailEventKey, CustomerEmailEventValue> customerEmailEventKafkaProducer(
        @Value("${kafka.bootstrap.servers}") String bootstrapServers,
        @Value("${schema.registry.url}") String schemaRegistryUrl
    ) {
        Properties props = buildPropertiesKeyAvroSerializer("reprocess-customer-email", bootstrapServers, schemaRegistryUrl);
        props.put(PARTITIONER_CLASS_CONFIG, "com.nutmeg.kafka.partitioner.NutmegKafkaPartitioner");
        return new KafkaProducer<>(props);
    }

    private Properties buildPropertiesKeyAvroSerializer(String clientId, String bootstrapServers, String schemaRegistryUrl) {
        final Properties props = buildProperties(clientId, bootstrapServers, schemaRegistryUrl);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        return props;
    }

    private Properties buildPropertiesKeyStringSerializer(String clientId, String bootstrapServers, String schemaRegistryUrl) {
        final Properties props = buildProperties(clientId, bootstrapServers, schemaRegistryUrl);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    private Properties buildProperties(String clientId, String bootstrapServers, String schemaRegistryUrl) {
        final Properties props = new Properties();
        props.put(CLIENT_ID_CONFIG, clientId);
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, 10);
        return props;
    }
}

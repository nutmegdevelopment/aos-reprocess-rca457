package com.example.migration.core;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.nutmeg.user.service.CustomerEmailEventValue;
import com.nutmeg.user.service.CustomerEmailEventKey;
import com.nutmeg.user.service.Metadata;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReprocessUserEmails {

    private static final String FILE_PATH_TO_EXECUTE = "/Users/iago.depaulaquirino/workspace/workspace-nutmeg/aos-reprocess-rca457/src/main/resources/rca-457/user-email.csv";

    private final Producer<CustomerEmailEventKey, CustomerEmailEventValue> customerEmailEventKafkaProducer;

    private static final String TOPIC = "com.nutmeg.customer.user-service.customer-email-by-user-id";

    @PostConstruct
    public void process() throws Exception {
        final List<CustomerEmail> customers = new CsvToBeanBuilder(new FileReader(FILE_PATH_TO_EXECUTE))
            .withType(CustomerEmail.class)
            .build()
            .parse();


        customers.forEach(customer -> {
            try {
                CustomerEmailEventKey key = CustomerEmailEventKey.newBuilder()
                    .setUserId(customer.getUuid())
                    .build();

                CustomerEmailEventValue value = CustomerEmailEventValue.newBuilder()
                    .setEmailAddress(customer.getEmail())
                    .setMetadata(metadata())
                    .build();

                customerEmailEventKafkaProducer.send(new ProducerRecord<>(TOPIC, key, value)).get();
                log.info("Published email for customerId={}", customer.getUuid());
            } catch (Exception e) {
                log.error("Failed to publish email for customerId={}", customer.getUuid());
            }
        });

        log.info("Finished to run the email script");
    }

    private Metadata metadata() {
        return Metadata.newBuilder()
            .setCreatedAt(Instant.now())
            .setCorrelationId(UUID.randomUUID().toString())
            .build();
    }

}

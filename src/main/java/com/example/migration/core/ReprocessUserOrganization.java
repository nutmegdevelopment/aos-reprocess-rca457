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
import com.nutmeg.user.service.Metadata;
import com.nutmeg.user.service.UserOrganisationEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReprocessUserOrganization {

    private static final String FILE_PATH_TO_EXECUTE = "/Users/iago.depaulaquirino/workspace/workspace-nutmeg/aos-reprocess-rca457/src/main/resources/rca-457/user-organization.csv";

    private static final String TOPIC = "com.nutmeg.customer.user-service.user-organisation-by-user-id";

    private final Producer<String, UserOrganisationEvent> userOrganisationKafkaProducer;

    @PostConstruct
    public void process() throws Exception {
        final List<CustomerOrganization> customers = new CsvToBeanBuilder(new FileReader(FILE_PATH_TO_EXECUTE))
            .withType(CustomerOrganization.class)
            .build()
            .parse();

        customers.forEach(customer -> {
            try {
                UserOrganisationEvent userOrganisationEvent = UserOrganisationEvent.newBuilder()
                    .setUserId(customer.getUuid())
                    .setCreatedAt(Instant.now())
                    .setOrganisation(customer.getOrganization())
                    .build();
                userOrganisationKafkaProducer.send(new ProducerRecord<>(TOPIC, customer.getUuid(), userOrganisationEvent)).get();
                log.info("Published organisation for customerId={}", customer.getUuid());
            } catch (Exception e) {
                log.error("Failed to publish organisation for customerId={}", customer.getUuid());
            }
        });
        log.info("Finished to run the organisation script");
    }
    
    private Metadata metadata() {
        return Metadata.newBuilder()
            .setCreatedAt(Instant.now())
            .setCorrelationId(UUID.randomUUID().toString())
            .build();
    }
}

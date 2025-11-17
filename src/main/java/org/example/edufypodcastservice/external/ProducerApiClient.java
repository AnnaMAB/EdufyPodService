package org.example.edufypodcastservice.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Service
public class ProducerApiClient {

    private final RestClient restClient;
    @Value("${producerExists.api.url}")
    private String producerExistsApiUrl;
    @Value("${producerAdd.api.url}")
    private String producerAddApiUrl;
    @Value("${producerRemove.api.url}")
    private String producerRemoveApiUrl;

    @Autowired
    public ProducerApiClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public boolean producerExists(UUID producerId) {
        try {
            Boolean producerExistsResponse = restClient.get()
                    .uri("http://localhost:8080/podcasts/api/producer/{id}/exists", producerId)//TODO --- rätt adress?
                    .retrieve()
                    .body(Boolean.class);
            return producerExistsResponse;
        } catch (RestClientException e) {
            throw new IllegalStateException("Failed to check producer " + producerId, e);
        }
    }


}

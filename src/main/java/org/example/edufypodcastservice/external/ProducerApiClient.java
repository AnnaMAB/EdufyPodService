package org.example.edufypodcastservice.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.edufypodcastservice.converters.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
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
    private final UserInfo userInfo;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public ProducerApiClient(RestClient.Builder restClientBuilder, UserInfo userInfo) {
        this.restClient = restClientBuilder.build();
        this.userInfo = userInfo;
    }

    public boolean producerExists(UUID producerId) {
        String role = userInfo.getRole();
        try {
            Boolean producerExistsResponse = restClient.get()
                    .uri(producerExistsApiUrl, producerId)
                    .retrieve()
                    .body(Boolean.class);
            return producerExistsResponse;
        } catch (RestClientException e) {
            throw new IllegalStateException("Failed to check producer " + producerId, e);
        }
    }


    public void removePodcastFromProducer(UUID podcastId, UUID producerId) {
        String role = userInfo.getRole();
        try {
            ResponseEntity<Void> response = restClient.put()
                    .uri(producerRemoveApiUrl, producerId, podcastId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            HttpStatusCode status = e.getStatusCode();
            String body = e.getResponseBodyAsString();
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(body);

                String message = json.path("message").asText();
                String path = json.path("path").asText();

                throw new IllegalStateException(
                        String.format("Failed to remove podcast from producer. Status %s, %s, Path:%s",
                                status, message, path), e);
            } catch (IOException parseEx) {
                throw new IllegalStateException("Failed to remove podcast from producer. Status=" + status + " body=" + body, e);
            }
        } catch (ResourceAccessException ex) {
            throw new IllegalStateException("Could not connect to producer service: " + ex.getMessage(), ex);
        } catch (RestClientException ex) {
            throw new IllegalStateException("Unexpected error calling producer service", ex);
        }
    }


    public void addPodcastToProducer(UUID podcastId, UUID producerId) {
        String role = userInfo.getRole();
        try {
            ResponseEntity<Void> response = restClient.put()
                    .uri(producerAddApiUrl, producerId, podcastId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            HttpStatusCode status = e.getStatusCode();
            String body = e.getResponseBodyAsString();
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(body);

                String message = json.path("message").asText();
                String path = json.path("path").asText();

                throw new IllegalStateException(
                        String.format("Failed to add podcast to producer. Status %s, %s, Path:%s",
                                status, message, path), e);
            } catch (IOException parseEx) {
                throw new IllegalStateException("Failed to add podcast to producer . Status=" + status + " body=" + body, e);
            }
        } catch (ResourceAccessException ex) {
            throw new IllegalStateException("Could not connect to producer service: " + ex.getMessage(), ex);
        } catch (RestClientException ex) {
            throw new IllegalStateException("Unexpected error calling producer service", ex);
        }
    }


}

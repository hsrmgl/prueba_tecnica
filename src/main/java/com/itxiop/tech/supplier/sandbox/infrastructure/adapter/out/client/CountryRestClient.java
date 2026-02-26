package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.out.client;

import com.itxiop.tech.supplier.sandbox.domain.port.out.CountryVerifierPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CountryRestClient implements CountryVerifierPort {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CountryRestClient(RestTemplate restTemplate, @Value("${country.service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    @Cacheable("countries")
    public boolean isBanned(String country) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(baseUrl + "/countries/" + country, Map.class);
            if (response == null) return false;
            Object banned = response.get("isBanned");
            if (banned instanceof Boolean b) return b;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

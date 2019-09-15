package com.philwin.finance.optionsingest.service;

import com.philwin.finance.optionsingest.model.TradierExpirationDatesResponse;
import com.philwin.finance.optionsingest.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Service
public class TradierOptionsService implements iAPIBroker{

    @Value(Constants.TRADIER_OPTIONS_EXPIRATIONS_API_RESOURCE_URL)
    private String expirationDatesResourceUrl;

    @Value("${tradier.access.token}")
    private String accessToken;

    @Override
    public String getData(Map<String, String> parameters) {
        RestTemplate restTemplate   =   new RestTemplate();
        UriComponentsBuilder uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(expirationDatesResourceUrl);
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            uriComponentsBuilder.queryParam(param.getKey(), param.getValue());
        }

        HttpEntity<?> entity = new HttpEntity<Object>(getHeaders());

        HttpEntity<String> response = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody().toString();
    }


    private  MultiValueMap<String, String> getHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add(HttpHeaders.USER_AGENT, "Spring's RestTemplate");
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.8");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken );
        return headers;
    }


}

package com.philwin.finance.optionsingest.service;

import com.philwin.finance.optionsingest.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Map;

@Service
public class TDAmeritradeOptionsService implements iAPIBroker{
    @Value("${tdameritrade.consumer.key}")
    private String consumerKey;

    @Value(Constants.TD_AMERITRADE_OPTIONS_API_RESOURCE_URL)
    private String resourceUrl;

    private Date processedDate;

    public TDAmeritradeOptionsService() {
        this.processedDate  =   new Date();
    }

    public String getData(Map<String, String> parameters) {
        RestTemplate restTemplate   =   new RestTemplate();
        parameters.put(Constants.TD_AMERITRADE_OPTIONS_API_KEY_VAR, this.consumerKey);
        UriComponentsBuilder    uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(resourceUrl);
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            uriComponentsBuilder.queryParam(param.getKey(), param.getValue());
        }

        return restTemplate.getForObject(uriComponentsBuilder.toUriString(), String.class);
    }

    @Override
    public String getHTTPVerb() {
        return "GET";
    }

    @Override
    public String getEndpoint() {
        return this.resourceUrl;
    }



}

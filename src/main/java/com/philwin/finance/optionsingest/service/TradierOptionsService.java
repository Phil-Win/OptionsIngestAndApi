package com.philwin.finance.optionsingest.service;

import com.philwin.finance.optionsingest.model.tradier.TradierOptionsChainResponse;
import com.philwin.finance.optionsingest.model.tradier.TradierOptionsDateResponse;
import com.philwin.finance.optionsingest.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@Slf4j
public class TradierOptionsService {

    @Value("${tradier.options.symbol.key}")
    private String SYMBOL_VARIABLE_NAME;

    @Value("${tradier.options.expiration.key}")
    private String EXPIRATION_VARIABLE_NAME;

    @Value("${tradier.options.greeks.key}")
    private String GREEKS_VARIABLE_NAME;

    @Value("${tradier.options.greeks.value}")
    private String GREEKS_VALUE;

    @Value(Constants.TRADIER_OPTIONS_EXPIRATIONS_API_RESOURCE_URL)
    private String expirationDatesResourceUrl;

    @Value(Constants.TRADIER_OPTIONS_CHAIN_API_RESOURCE_URL)
    private String optionsChainResourceUrl;

    @Value("${tradier.access.token}")
    private String accessToken;

    public List<TradierOptionsChainResponse> getData(String stock) {
        List<TradierOptionsChainResponse> returnList    =   new ArrayList<TradierOptionsChainResponse>();
        List<String> datesToProcess =   getListOfExpirationDates(stock);
        log.info("Got the dates to process for the stock: " + stock + " = " + datesToProcess.toString());
        for (String dates : datesToProcess) {
            log.info("Processing the date: " + dates);
            returnList.add(getOptionsChain(stock, dates));
        }
        return returnList;
    }

    private TradierOptionsChainResponse getOptionsChain(String stock, String dates) {
        Map<String, String>     queryParameters =   new HashMap<String, String>();
        RestTemplate            restTemplate    =   new RestTemplate();

        queryParameters.put(this.SYMBOL_VARIABLE_NAME, stock);
        queryParameters.put(this.EXPIRATION_VARIABLE_NAME, dates);
        queryParameters.put(this.GREEKS_VARIABLE_NAME, GREEKS_VALUE);

        UriComponentsBuilder uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(optionsChainResourceUrl);

        for (Map.Entry<String, String> param : queryParameters.entrySet()) {
            uriComponentsBuilder.queryParam(param.getKey(), param.getValue());
        }

        HttpEntity<TradierOptionsChainResponse> response = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                getHeaders(),
                TradierOptionsChainResponse.class
        );

        return response.getBody();
    }

    private List<String> getListOfExpirationDates(String stockOfInterest) {
        List<String>            returnList      =   new ArrayList<String>();
        Map<String, String>     queryParameters =   new HashMap<String, String>();
        RestTemplate            restTemplate    =   new RestTemplate();

        queryParameters.put(this.SYMBOL_VARIABLE_NAME, stockOfInterest);
        UriComponentsBuilder uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(expirationDatesResourceUrl);

        for (Map.Entry<String, String> param : queryParameters.entrySet()) {
            uriComponentsBuilder.queryParam(param.getKey(), param.getValue());
        }

        HttpEntity<TradierOptionsDateResponse> response = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                getHeaders(),
                TradierOptionsDateResponse.class
        );

        return Objects.requireNonNull(response.getBody()).getExpirations().getDate();
    }


    private  HttpEntity<?> getHeaders() {
        //MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        HttpHeaders header  =   new HttpHeaders();
        header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        header.setBearerAuth(accessToken);
        return new HttpEntity<>(body, header);
    }


}

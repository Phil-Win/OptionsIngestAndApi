package com.philwin.finance.optionsingest.service;

import com.google.common.util.concurrent.RateLimiter;
import com.philwin.finance.optionsingest.model.tradier.TradierOptionsChainResponse;
import com.philwin.finance.optionsingest.model.tradier.TradierOptionsDateResponse;
import com.philwin.finance.optionsingest.model.tradier.clock.Clock;
import com.philwin.finance.optionsingest.model.tradier.clock.ClockResponse;
import com.philwin.finance.optionsingest.model.tradier.stock.TradierStockQuoteResponse;
import com.philwin.finance.optionsingest.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.philwin.finance.optionsingest.exception.UnknownClassException;

import java.util.*;

@Service
@Slf4j
public class TradierOptionsService {

    @Value(Constants.TRADIER_SYMBOL)
    private String SYMBOL_VARIABLE_NAME;

    @Value(Constants.TRADIER_EXPIRATION)
    private String EXPIRATION_VARIABLE_NAME;

    @Value(Constants.TRADIER_GREEKS)
    private String GREEKS_VARIABLE_NAME;

    @Value(Constants.TRADIER_GREEKS_VALUE)
    private String GREEKS_VALUE;

    @Value(Constants.TRADIER_OPTIONS_EXPIRATIONS_API_RESOURCE_URL)
    private String expirationDatesResourceUrl;

    @Value(Constants.TRADIER_OPTIONS_CHAIN_API_RESOURCE_URL)
    private String optionsChainResourceUrl;

    @Value(Constants.TRADIER_MARKET_CLOCK_RESOURCE_URL)
    private String marketClockResourceUrl;
    @Value(Constants.TRADIER_STOCK_QUOTE_URL)
    private String stockQuoteUrl;

    @Value("${tradier.access.token}")
    private String accessToken;

    private final RateLimiter rateLimiter = RateLimiter.create(1.0);


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
        TradierOptionsChainResponse tradierOptionsChainResponse =   null;
        Map<String, String>     queryParameters =   new HashMap<String, String>();
        queryParameters.put(this.SYMBOL_VARIABLE_NAME, stock);
        queryParameters.put(this.EXPIRATION_VARIABLE_NAME, dates);
        queryParameters.put(this.GREEKS_VARIABLE_NAME, GREEKS_VALUE);
        UriComponentsBuilder uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(optionsChainResourceUrl);

        for (Map.Entry<String, String> param : queryParameters.entrySet()) {
            uriComponentsBuilder.queryParam(param.getKey(), param.getValue());
        }
        tradierOptionsChainResponse = (TradierOptionsChainResponse) makeTradierApiCallUntilComplete(uriComponentsBuilder.toUriString(), TradierOptionsChainResponse.class);
        Assert.notNull(tradierOptionsChainResponse, "List of Expirationn Dates should not be null throwing exception...");

        return tradierOptionsChainResponse;
    }

    private List<String> getListOfExpirationDates(String stockOfInterest) {
        Map<String, String>         queryParameters =   new HashMap<String, String>();
        TradierOptionsDateResponse  tradierOptionsDateResponse  =   null;
        queryParameters.put(this.SYMBOL_VARIABLE_NAME, stockOfInterest);
        UriComponentsBuilder uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(expirationDatesResourceUrl);

        for (Map.Entry<String, String> param : queryParameters.entrySet()) {
            uriComponentsBuilder.queryParam(param.getKey(), param.getValue());
        }
        tradierOptionsDateResponse = (TradierOptionsDateResponse) makeTradierApiCallUntilComplete(uriComponentsBuilder.toUriString(), TradierOptionsDateResponse.class);
        Assert.notNull(tradierOptionsDateResponse, "List of Expirationn Dates should not be null throwing exception...");

        return tradierOptionsDateResponse.getExpirations().getDate();
    }

    public Boolean isMarketOpen() {
        ClockResponse clockResponse =   null;
        UriComponentsBuilder uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(marketClockResourceUrl);
        clockResponse     =   (ClockResponse) makeTradierApiCallUntilComplete(uriComponentsBuilder.toUriString(), ClockResponse.class);
        Assert.notNull(clockResponse, "Clock Response cannot be null... throwing exception");

        return clockResponse.getClock().getState().equalsIgnoreCase(Constants.TRADIER_MARKET_OPEN);
    }

    public TradierStockQuoteResponse getStockQuotes(List<String> listOfStocks) {
        TradierStockQuoteResponse   tradierStockQuoteResponse   =   null;
        UriComponentsBuilder uriComponentsBuilder    =   UriComponentsBuilder.fromUriString(stockQuoteUrl);

        uriComponentsBuilder.queryParam(Constants.TRADIER_SYMBOLS, String.join(",", listOfStocks));
        log.info("Printing uri... {}", uriComponentsBuilder.toUriString());
        tradierStockQuoteResponse   =   (TradierStockQuoteResponse) makeTradierApiCallUntilComplete(uriComponentsBuilder.toUriString(), TradierStockQuoteResponse.class);
        Assert.notNull(tradierStockQuoteResponse, "Stock Quote response  cannot be null... throwing exception");
        return tradierStockQuoteResponse;
    }

    private Object makeTradierApiCall(String uriString, Class returnClass) throws UnknownClassException {
        rateLimiter.acquire();
        RestTemplate            restTemplate    =   new RestTemplate();
        log.info("Making a Tradier API call with uriString=" + uriString + " and returnClass=" + returnClass);
        try {
            if (returnClass.equals(ClockResponse.class)) {
                ClockResponse returnClockResponse   =   makeClockResponseAPICall(uriString);
                Assert.notNull(returnClockResponse, "Like seriously...");
                return returnClockResponse;
            } else if (returnClass.equals(TradierOptionsDateResponse.class)) {
                TradierOptionsDateResponse  tradierOptionsDateResponse  =   makeTradierOptionsDateResponse(uriString);
                Assert.notNull(tradierOptionsDateResponse);
                return tradierOptionsDateResponse;
            } else if (returnClass.equals(TradierOptionsChainResponse.class)) {
                TradierOptionsChainResponse tradierOptionsChainResponse = makeTradierOptionsChainResponse(uriString);
                Assert.notNull(tradierOptionsChainResponse);
                return tradierOptionsChainResponse;
            } else if (returnClass.equals(TradierStockQuoteResponse.class)) {
                TradierStockQuoteResponse tradierStockQuoteResponse =   makeTradierStockQuoteReponse(uriString);
                Assert.notNull(tradierStockQuoteResponse);
                return tradierStockQuoteResponse;
            } else {
                throw new UnknownClassException("What is this class even... " + returnClass.getName());
            }
        } catch (Exception e) {
            log.info("Exception thrown... returning null");
            return null;
        }
    }

    private Object makeTradierApiCallUntilComplete(String uriString, Class returnClass) {
        try {
            Object returnObject =   makeTradierApiCall(uriString, returnClass);
            if (returnObject == null) {
                try {
                    wait(5);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                makeTradierApiCallUntilComplete(uriString, returnClass);
            } else {
                return returnObject;
            }
        } catch (Exception e) {
            log.info("Exception found when trying to make Tradier API call... trying again");
            try {
                wait(5);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            makeTradierApiCallUntilComplete(uriString, returnClass);
        }
        return null; //should never get here
    }


    private  HttpEntity<?> getHeaders() {
        //MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        HttpHeaders header  =   new HttpHeaders();
        header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        header.setBearerAuth(accessToken);
        return new HttpEntity<>(body, header);
    }

    private ClockResponse makeClockResponseAPICall(String uriString) {
        RestTemplate            restTemplate    =   new RestTemplate();
        HttpEntity<ClockResponse> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                getHeaders(),
                ClockResponse.class
        );
        log.info("API call to Clock successful!");
        return response.getBody();
    }

    private TradierOptionsDateResponse makeTradierOptionsDateResponse(String uriString) {
        RestTemplate            restTemplate    =   new RestTemplate();
        HttpEntity<TradierOptionsDateResponse> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                getHeaders(),
                TradierOptionsDateResponse.class
        );
        log.info("API call to Options date");
        return response.getBody();
    }

    private TradierOptionsChainResponse makeTradierOptionsChainResponse(String uriString) {
        RestTemplate            restTemplate    =   new RestTemplate();
        HttpEntity<TradierOptionsChainResponse> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                getHeaders(),
                TradierOptionsChainResponse.class
        );
        log.info("API call to Options Chain!");
        return response.getBody();
    }

    private TradierStockQuoteResponse makeTradierStockQuoteReponse(String uriString) {
        RestTemplate            restTemplate    =   new RestTemplate();
        HttpEntity<TradierStockQuoteResponse> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                getHeaders(),
                TradierStockQuoteResponse.class
        );
        log.info("API call to Stock Quote!");
        return response.getBody();
    }




}
package com.philwin.finance.optionsingest.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philwin.finance.optionsingest.exception.MissingParameterException;
import com.philwin.finance.optionsingest.model.tradier.TradierOptionsChainResponse;
import com.philwin.finance.optionsingest.model.tradier.stock.TradierStockQuoteResponse;
import com.philwin.finance.optionsingest.service.TradierOptionsService;
import com.philwin.finance.optionsingest.util.DataWriterUtil;
import com.philwin.finance.optionsingest.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class TradierOptionsAutoDownloader {
    @Autowired
    private TradierOptionsService tradierOptionsService;

    @Autowired
    private Environment env;

    @Value("${tradier.options.historical.landingzone}")
    private String historicalLandingZone;

    private final String    SYMBOL_VARIABLE_PREFIX              ="tradier.options.symbol.";
    private List<String> listOfStocks;


    @PostConstruct
    public void init() {
        PropertiesUtil  propertiesUtil  =   new PropertiesUtil();
        try {
            listOfStocks    = propertiesUtil.getListOfProperties(SYMBOL_VARIABLE_PREFIX, env);
        } catch (MissingParameterException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 15 12 * * *")
    public void downloadDataFromTdAmeritradeOptionsApi() {
        Date date   =   new Date();
        List<TradierOptionsChainResponse> optionsData;
        TradierStockQuoteResponse   stockData;
        log.info("Processing Daily set of stock information from the Tradier Controller");
        if (tradierOptionsService.isMarketOpen()) {
            stockData   =   tradierOptionsService.getStockQuotes(listOfStocks);
            if (DataWriterUtil.storeData(stockData, historicalLandingZone + File.pathSeparatorChar + "all_stock_data_" + date)) {
                log.info("Successfully stored stock data for {}", String.join(", ", listOfStocks));
            }
            for (String stock : listOfStocks) {
                optionsData = tradierOptionsService.getData(stock);
                if (DataWriterUtil.storeData(optionsData, historicalLandingZone + File.pathSeparatorChar + stock + "_options_processed_on_" + date)) {
                    log.info("Successfully stored options data for {}", stock);
                }
            }
        }
    }
}

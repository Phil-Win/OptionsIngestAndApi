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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
//@PropertySource("classpath:application.properties")
public class TradierOptionsAutoDownloader {
    @Autowired
    private TradierOptionsService tradierOptionsService;

    @Autowired
    private Environment env;

    @Value("${tradier.stocks.symbols.to.download.comma.separated}")
    private String commaSeparatedStockSymbols;

    @Value("${tradier.options.historical.landingzone}")
    private String historicalLandingZone;

    private final String    SYMBOL_VARIABLE_PREFIX              ="tradier.options.symbol.";
    private List<String> listOfStocks;


    @PostConstruct
    public void init() {
//        PropertiesUtil  propertiesUtil  =   new PropertiesUtil();
//        try {
//            listOfStocks    = propertiesUtil.getListOfProperties(SYMBOL_VARIABLE_PREFIX, env);
//        } catch (MissingParameterException e) {
//            e.printStackTrace();
//        }
        listOfStocks    = Arrays.asList(commaSeparatedStockSymbols.split(","));
    }

    @Scheduled(cron = "0 0 12 * * *")
//    @Scheduled(fixedRate= 10000000)
    public void downloadStockData() {
        DateFormat dateFormat   =   new SimpleDateFormat("dd_MM_yyyy");
        Date date   =   new Date();
        List<TradierOptionsChainResponse> optionsData;
        TradierStockQuoteResponse   stockData;
        log.info("Processing Daily set of stock information from the Tradier Controller");
        if (tradierOptionsService.isMarketOpen()) {
            log.info("Printing the list of stocks... {}", String.join(", ", listOfStocks));
            stockData   =   tradierOptionsService.getStockQuotes(listOfStocks);
            if (DataWriterUtil.storeData(stockData, historicalLandingZone + File.separator +
                    dateFormat.format(date) + File.separator + "all_stock_data_" + dateFormat.format(date) + ".json")) {
                log.info("Successfully stored stock data for {}", String.join(", ", listOfStocks));
            } else {
                log.info("Failed to store stock data");
            }
//            for (String stock : listOfStocks) {
//                optionsData = tradierOptionsService.getData(stock);
//                if (DataWriterUtil.storeData(optionsData, historicalLandingZone + File.separator +
//                        dateFormat.format(date) + File.separator + stock + "_options_processed_on_" + dateFormat.format(date) + ".json")) {
//                    log.info("Successfully stored options data for {}", stock);
//                } else {
//                    log.info("Failed to store options data for {}", stock);
//                }
//            }
        }
    }

    @Scheduled(cron = "0 15 12 * * *")
//    @Scheduled(fixedRate= 10000000)
    public void downloadOptionsData() {
        DateFormat dateFormat   =   new SimpleDateFormat("dd_MM_yyyy");
        Date date   =   new Date();
        List<TradierOptionsChainResponse> optionsData;
        TradierStockQuoteResponse   stockData;
        log.info("Processing Daily set of Options information from the Tradier Controller");
        if (tradierOptionsService.isMarketOpen()) {
            log.info("Printing the list of stocks to process... {}", String.join(", ", listOfStocks));
//            stockData   =   tradierOptionsService.getStockQuotes(listOfStocks);
//            if (DataWriterUtil.storeData(stockData, historicalLandingZone + File.separator +
//                    dateFormat.format(date) + File.separator + "all_stock_data_" + dateFormat.format(date) + ".json")) {
//                log.info("Successfully stored stock data for {}", String.join(", ", listOfStocks));
//            } else {
//                log.info("Failed to store stock data");
//            }
            for (String stock : listOfStocks) {
                optionsData = tradierOptionsService.getData(stock);
                if (DataWriterUtil.storeData(optionsData, historicalLandingZone + File.separator +
                        dateFormat.format(date) + File.separator + stock + "_options_processed_on_" + dateFormat.format(date) + ".json")) {
                    log.info("Successfully stored options data for {}", stock);
                } else {
                    log.info("Failed to store options data for {}", stock);
                }
            }
        }
    }
}

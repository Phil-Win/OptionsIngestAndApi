package com.philwin.finance.optionsingest.controller;


import com.philwin.finance.optionsingest.exception.MissingParameterException;
import com.philwin.finance.optionsingest.model.tradier.TradierOptionsChainResponse;
import com.philwin.finance.optionsingest.service.TradierOptionsService;
import com.philwin.finance.optionsingest.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class TradierOptionsDownloadController {
    @Autowired
    private TradierOptionsService tradierOptionsService;

    @Autowired
    private Environment env;

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

    @Scheduled(fixedDelay = 100000)
    public void downloadDataFromTdAmeritradeOptionsApi() {
        log.info("Hello world from the Tradier Controller" + System.currentTimeMillis() / 1000);
        for (String stock : listOfStocks) {
            log.info("Processing the stock:  " + stock);
            for (TradierOptionsChainResponse response : tradierOptionsService.getData(stock)) {
                log.info(response.toString());
            }
            log.info("Finished processing the stock:  " + stock);
        }
    }
}

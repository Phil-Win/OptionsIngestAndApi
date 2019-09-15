package com.philwin.finance.optionsingest.controller;


import com.philwin.finance.optionsingest.exception.MissingParameterException;
import com.philwin.finance.optionsingest.service.TDAmeritradeOptionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class TDAmeritradeOptionsDownloadController {

    @Autowired
    private TDAmeritradeOptionsService tdAmeritradeOptionsService;

    @Autowired
    private Environment env;

    private final String    SYMBOL_VARIABLE_PREFIX="tdameritrade.options.downloader.symbol.";
    private final String    STRIKE_COUNT_VARIABLE_PREFIX="tdameritrade.options.downloader.strikecount.";
    private final String    KEY =   "key";
    private String      SYMBOL_VARIABLE;
    private String      STRIKE_COUNT_VARIABLE;


    private List<HashMap<String, String>> setOfParametersToRun;

    @PostConstruct
    public void init() {
        try {
            this.setOfParametersToRun = new ArrayList<HashMap<String, String>>();

            loadKeys();

            int counter = 1;
            this.setOfParametersToRun = new ArrayList<HashMap<String, String>>();
            while (env.getProperty(SYMBOL_VARIABLE_PREFIX + counter) != null) {
                setOfParametersToRun.add(getParametersFromIndex(counter));
                counter++;
            }
        }catch (MissingParameterException e) {
            //TODO: Catch it better
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 100000)
    public void downloadDataFromTdAmeritradeOptionsApi() {
        log.info("Hello world! " + System.currentTimeMillis() / 1000);
        log.info("Printing the query keys " + this.SYMBOL_VARIABLE + " and the strike: " + this.STRIKE_COUNT_VARIABLE);
        for (HashMap<String, String> parameterSet: setOfParametersToRun) {
            log.info(tdAmeritradeOptionsService.getData(parameterSet));
        }
    }

    private HashMap<String, String> getParametersFromIndex(Integer index) throws MissingParameterException {
        HashMap<String, String> returnMap   =   new HashMap<String, String>();
        if (env.getProperty(SYMBOL_VARIABLE_PREFIX + index) == null || env.getProperty(STRIKE_COUNT_VARIABLE_PREFIX + index) == null) {
            throw new MissingParameterException("Missing a Parameter for Index : " + index +
                    " Symbol: " + env.getProperty(SYMBOL_VARIABLE_PREFIX + index) +
                    " Strike Count: " + env.getProperty(STRIKE_COUNT_VARIABLE_PREFIX + index) );
        }
        returnMap.put(SYMBOL_VARIABLE, env.getProperty(SYMBOL_VARIABLE_PREFIX + index));
        returnMap.put(STRIKE_COUNT_VARIABLE, env.getProperty(STRIKE_COUNT_VARIABLE_PREFIX + index));

        log.info("Successfully loaded the properties for index: " + index );
        for(Map.Entry<String, String> paramter : returnMap.entrySet()) {
            log.info("Key:Val = " + paramter.getKey() + ":" + paramter.getValue());
        }
        return returnMap;
    }

    private void loadKeys() throws MissingParameterException{
        this.SYMBOL_VARIABLE        =   env.getProperty(SYMBOL_VARIABLE_PREFIX          +   KEY);
        this.STRIKE_COUNT_VARIABLE  =   env.getProperty(STRIKE_COUNT_VARIABLE_PREFIX    +   KEY);
        if (this.SYMBOL_VARIABLE != null &&  this.STRIKE_COUNT_VARIABLE != null) {
            log.info("Successfully loaded the Keys!");
        } else {
            throw new MissingParameterException("Failed to load the keys: SYMBOL_VARIABLE=" + this.SYMBOL_VARIABLE
                    + " STRIKE_COUNT_VARIABLE=" + STRIKE_COUNT_VARIABLE);
        }
    }


}

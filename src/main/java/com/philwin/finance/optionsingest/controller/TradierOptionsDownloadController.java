package com.philwin.finance.optionsingest.controller;


import com.philwin.finance.optionsingest.exception.MissingParameterException;
import com.philwin.finance.optionsingest.service.TradierOptionsService;
import com.philwin.finance.optionsingest.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TradierOptionsDownloadController {
    @Autowired
    private TradierOptionsService tradierOptionsService;

    @Autowired
    private Environment env;

    private final String    SYMBOL_VARIABLE_PREFIX="tradier.options.expirations.symbol.";
    private final String    INCLUDE_ALL_ROOTS_VARIABLE_PREFIX="tradier.options.expirations.include.roots.";
    private final String    STRIKES_VARIABLE_PREFIX="tradier.options.expirations.strikes.";
    private List<HashMap<String, String>> setOfParametersToRun;


    @PostConstruct
    public void init() {
        List<String> parameterPrefixesToProcess    =   new ArrayList<String>();
        PropertiesUtil  propertiesUtil  =   new PropertiesUtil();
        parameterPrefixesToProcess.add(SYMBOL_VARIABLE_PREFIX);
        parameterPrefixesToProcess.add(INCLUDE_ALL_ROOTS_VARIABLE_PREFIX);
        parameterPrefixesToProcess.add(STRIKES_VARIABLE_PREFIX);
        setOfParametersToRun    = propertiesUtil.getListOfParameterSets(parameterPrefixesToProcess, env);
    }

    @Scheduled(fixedDelay = 100000)
    public void downloadDataFromTdAmeritradeOptionsApi() {
        log.info("Hello world from the Tradier Controller" + System.currentTimeMillis() / 1000);
        for (HashMap<String, String> parameterSet: setOfParametersToRun) {
            StringBuilder logToPrint    =   new StringBuilder();
            logToPrint.append("Running the job with the parameters... ");
            for (Map.Entry<String, String> parameter : parameterSet.entrySet()) {
                logToPrint.append(" ").append(parameter.getKey()).append(":").append(parameter.getValue()).append(" ");
            }
            log.info(logToPrint.toString());
            log.info(tradierOptionsService.getData(parameterSet));
        }
    }
}

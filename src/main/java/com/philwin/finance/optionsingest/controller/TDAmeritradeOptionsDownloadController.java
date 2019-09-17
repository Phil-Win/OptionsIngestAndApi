package com.philwin.finance.optionsingest.controller;


import com.philwin.finance.optionsingest.exception.MissingParameterException;
import com.philwin.finance.optionsingest.service.TDAmeritradeOptionsService;
import com.philwin.finance.optionsingest.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class TDAmeritradeOptionsDownloadController {

    @Autowired
    private TDAmeritradeOptionsService tdAmeritradeOptionsService;

    @Autowired
    private Environment env;

    private final String    SYMBOL_VARIABLE_PREFIX="tdameritrade.options.downloader.symbol.";
    private final String    STRIKE_COUNT_VARIABLE_PREFIX="tdameritrade.options.downloader.strikecount.";
    private List<HashMap<String, String>> setOfParametersToRun;

    @PostConstruct
    public void init() {
        List<String> parameterPrefixesToProcess    =   new ArrayList<String>();
        PropertiesUtil  propertiesUtil  =   new PropertiesUtil();
        parameterPrefixesToProcess.add(SYMBOL_VARIABLE_PREFIX);
        parameterPrefixesToProcess.add(STRIKE_COUNT_VARIABLE_PREFIX);
        setOfParametersToRun    = propertiesUtil.getListOfParameterSets(parameterPrefixesToProcess, env);
    }

    @Scheduled(fixedDelay = 100000)
    public void downloadDataFromTdAmeritradeOptionsApi() {
        log.info("Hello world From the TD Application " + System.currentTimeMillis() / 1000);
        for (HashMap<String, String> parameterSet: setOfParametersToRun) {
            StringBuilder logToPrint    =   new StringBuilder();
            logToPrint.append("Running the job with the parameters... ");
            for (Map.Entry<String, String> parameter : parameterSet.entrySet()) {
                logToPrint.append(" ").append(parameter.getKey()).append(":").append(parameter.getValue()).append(" ");
            }
            log.info(logToPrint.toString());
            log.info(tdAmeritradeOptionsService.getData(parameterSet));
        }
    }

}

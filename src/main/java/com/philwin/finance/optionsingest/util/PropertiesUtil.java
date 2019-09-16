package com.philwin.finance.optionsingest.util;

import com.philwin.finance.optionsingest.exception.MissingParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class PropertiesUtil {

    private final String KEY;

    public PropertiesUtil() {
        KEY = "key";
    }

    public List<HashMap<String, String>> getListOfParameterSets(List<String> listOfParameterPrefixes, Environment env) {
        List<HashMap<String, String>> returnList    =   new ArrayList<HashMap<String, String>>();
        int counter =   1;
        try {
            while ( env.getProperty(listOfParameterPrefixes.get(0) + counter) != null) {
                returnList.add(getParametersFromIndex(listOfParameterPrefixes, counter, env));
                counter++;
            }
        } catch (MissingParameterException e) {
            //TODO: Catch it better
            e.printStackTrace();
        }
        return returnList;
    }

    private HashMap<String, String> getParametersFromIndex(List<String> listOfParameterPrefixes, Integer index, Environment env) throws MissingParameterException {
        HashMap<String, String> returnMap   =   new HashMap<String, String>();
        String parameterKey;
        String parameterValue;
        log.info("Got to the getParametersFromIndex method");
        for (String paramPrefix : listOfParameterPrefixes) {
            log.info("processing the parameter: " + paramPrefix);
            if (env.getProperty(paramPrefix + index) == null) {
                throw new MissingParameterException("The parameter was not found in the application.properties : " + paramPrefix + index);
            } else if (env.getProperty(paramPrefix + KEY) == null){
                throw new MissingParameterException("The parameter was not found in the application.properties : " + paramPrefix + KEY);
            } else {
                parameterKey        =   env.getProperty(paramPrefix + KEY);
                parameterValue      =   env.getProperty(paramPrefix + index);
                returnMap.put(parameterKey, parameterValue);
            }
        }
        return returnMap;
    }
}



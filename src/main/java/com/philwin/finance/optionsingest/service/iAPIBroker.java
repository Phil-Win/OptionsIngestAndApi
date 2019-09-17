package com.philwin.finance.optionsingest.service;


import java.util.List;
import java.util.Map;

interface iAPIBroker {
    public String getData(Map<String, String> parameters);
}

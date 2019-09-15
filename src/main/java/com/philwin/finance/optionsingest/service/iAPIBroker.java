package com.philwin.finance.optionsingest.service;

import com.philwin.finance.optionsingest.model.iOption;

import java.util.List;
import java.util.Map;

interface iAPIBroker {
    public String getData(Map<String, String> parameters);
}

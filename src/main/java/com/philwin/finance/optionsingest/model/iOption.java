package com.philwin.finance.optionsingest.model;

public interface iOption {
    public iStock getUnderlyingStock();
    public String getContractType();
    public String getStrike();
    public String getBid();
    public String getAsk();
    public String getHighPrice();
    public String getLowPrice();
    public String getDaysToExpiration();
    public String getVolume();
    public String getOpenInterest();
    public String getDelta();
    public String getVolatility();
    public String getTheta();
    public String getGamma();
    public String getProcessedTime();
    public String getExpirationDate();
}

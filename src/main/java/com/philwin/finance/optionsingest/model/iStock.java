package com.philwin.finance.optionsingest.model;

public interface iStock {
    public String getSymbol();
    public String getDescription();
    public String getBid();
    public String getAsk();
    public String getHighPrice();
    public String getLowPrice();
    public String getVolume();
}

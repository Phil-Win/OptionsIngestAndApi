
package com.philwin.finance.optionsingest.model.tradier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class Option {

    private Double ask;
    private Long askDate;
    private String askexch;
    private Long asksize;
    private Long averageVolume;
    private Double bid;
    private Long bidDate;
    private String bidexch;
    private Long bidsize;
    private Long change;
    private Long changePercentage;
    private Object close;
    private Long contractSize;
    private String description;
    private String exch;
    private String expirationDate;
    private String expirationType;
    private Greeks greeks;
    private Object high;
    private Double last;
    private Long lastVolume;
    private Object low;
    private Object open;
    private Long openInterest;
    private String optionType;
    private Double prevclose;
    private String rootSymbol;
    private Long strike;
    private String symbol;
    private Long tradeDate;
    private String type;
    private String underlying;
    private Long volume;
    private Long week52High;
    private Long week52Low;

}

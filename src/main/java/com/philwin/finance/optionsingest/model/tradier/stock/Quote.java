
package com.philwin.finance.optionsingest.model.tradier.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class Quote {

    private Double ask;
    @JsonProperty("ask_date")
    private Long askDate;
    private String askexch;
    private Long asksize;
    @JsonProperty("average_volume")
    private Long averageVolume;
    private Double bid;
    @JsonProperty("bid_date")
    private Long bidDate;
    private String bidexch;
    private Long bidsize;
    private Double change;
    @JsonProperty("change_percentage")
    private Double changePercentage;
    private Object close;
    private String description;
    private String exch;
    private Double high;
    private Double last;
    @JsonProperty("last_volume")
    private Long lastVolume;
    private Double low;
    private Double open;
    private Double prevclose;
    @JsonProperty("root_symbols")
    private String rootSymbols;
    private String symbol;
    @JsonProperty("trade_date")
    private Long tradeDate;
    private String type;
    private Long volume;
    @JsonProperty("week_52_high")
    private Double week52High;
    @JsonProperty("week_52_low")
    private Long week52Low;

}

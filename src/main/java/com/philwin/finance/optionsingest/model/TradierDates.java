package com.philwin.finance.optionsingest.model;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class TradierDates {
    @Setter
    @Getter
    private List<String> date;
}

package com.philwin.finance.optionsingest.model;

import lombok.*;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class TradierExpirationDatesResponse {
    @Setter
    @Getter
    private TradierDates date;

}

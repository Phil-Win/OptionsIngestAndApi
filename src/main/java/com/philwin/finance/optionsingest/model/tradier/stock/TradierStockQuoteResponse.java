
package com.philwin.finance.optionsingest.model.tradier.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class TradierStockQuoteResponse {

    private Quotes quotes;

}

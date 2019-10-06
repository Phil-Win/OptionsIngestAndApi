
package com.philwin.finance.optionsingest.model.tradier.stock;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class Quotes {

    private List<Quote> quote;

}

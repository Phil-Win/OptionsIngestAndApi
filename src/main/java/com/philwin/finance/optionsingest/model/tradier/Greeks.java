
package com.philwin.finance.optionsingest.model.tradier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class Greeks {

    private Double askIv;
    private Long bidIv;
    private Double delta;
    private Double gamma;
    private Double midIv;
    private Double phi;
    private Double rho;
    private Double smvVol;
    private Double theta;
    private String updatedAt;
    private Double vega;

}

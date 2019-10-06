
package com.philwin.finance.optionsingest.model.tradier.clock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class Clock {

    private String date;
    private String description;
    @JsonProperty("next_change")
    private String nextChange;
    @JsonProperty("next_state")
    private String nextState;
    private String state;
    private Long timestamp;

}

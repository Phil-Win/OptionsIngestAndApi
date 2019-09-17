
package com.philwin.finance.optionsingest.model.tradier;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class Options {

    private List<Option> option;

}

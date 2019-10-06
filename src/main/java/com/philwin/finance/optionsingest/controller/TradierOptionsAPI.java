package com.philwin.finance.optionsingest.controller;


import com.philwin.finance.optionsingest.exception.MissingParameterException;
import com.philwin.finance.optionsingest.model.tradier.TradierOptionsChainResponse;
import com.philwin.finance.optionsingest.model.tradier.stock.TradierStockQuoteResponse;
import com.philwin.finance.optionsingest.service.TradierOptionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class TradierOptionsAPI {

    @Autowired
    private TradierOptionsService tradierOptionsService;

    @GetMapping("/options/tradier/{symbol}")
    public ResponseEntity<List<TradierOptionsChainResponse>> getOptionsChainBySymbol(@PathVariable (value = "symbol") String stockSymbol) throws MissingParameterException {
        try {
            return ResponseEntity.ok().body(tradierOptionsService.getData(stockSymbol));
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/marketstatus")
    public ResponseEntity<Boolean> getMarketStatus() throws MissingParameterException {
        try {
            return ResponseEntity.ok().body(tradierOptionsService.isMarketOpen());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock/quotes")
    public ResponseEntity<TradierStockQuoteResponse> getStockQuotes(@PathVariable (value = "symbols") String commaSeparatedStockSymbols) throws MissingParameterException {
        try {
            return ResponseEntity.ok().body(tradierOptionsService.getStockQuotes(new ArrayList<String>(Collections.singleton(commaSeparatedStockSymbols))));
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }


    //@GetMapping("/options/tradier/historical/availabledates")

    //@GetMapping("/options/tradier/historical/{date}")

}

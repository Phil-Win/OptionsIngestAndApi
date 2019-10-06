package com.philwin.finance.optionsingest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philwin.finance.optionsingest.model.tradier.TradierOptionsChainResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class DataWriterUtil {
    public static Boolean storeData(Object dataObject, String storageLocation) {
        log.info("Storing object in : {}", storageLocation);
        BufferedWriter writer   =   null;
        ObjectMapper mapper     =   new ObjectMapper();

        try {
            log.info("Storing object in : {}", storageLocation);
            log.info("Processing the object: {}", mapper.writeValueAsString(dataObject));
            writer   =   new BufferedWriter(new FileWriter(storageLocation));
            writer.write(mapper.writeValueAsString(dataObject));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

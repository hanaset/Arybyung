package com.how.arybyungprovider.service.validation;

import com.how.muchcommon.model.type.MarketName;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ValidationFactory {
    private final DanggnValidation danggnValidation;
    private final JoonggonaraValidation joonggonaraValidation;
    private final BunjangValidation bunjangValidation;

    public ValidationMarket getInstanseBySite(MarketName marketName) {
        if(marketName.getName().equals(MarketName.joonggonara)) {
            return joonggonaraValidation;
        }

        if(marketName.getName().equals(MarketName.bunjang)) {
            return bunjangValidation;
        }

        if(marketName.getName().equals(MarketName.danggn)) {
            return danggnValidation;
        }

        throw new IllegalArgumentException("MarketName enum error!");

    }
}

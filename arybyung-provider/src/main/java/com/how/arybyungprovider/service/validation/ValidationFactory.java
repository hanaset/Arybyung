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

    public ValidationMarket getInstanceBySite(MarketName marketName) {

        switch (marketName) {
            case joonggonara:
                return joonggonaraValidation;
            case bunjang:
                return bunjangValidation;
            case danggn:
                return danggnValidation;
        }

        throw new IllegalArgumentException("MarketName enum error!");
    }
}

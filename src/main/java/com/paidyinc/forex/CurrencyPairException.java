package com.paidyinc.forex;

import com.paidyinc.forex.model.CurrencyPair;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
public class CurrencyPairException extends RuntimeException {
    private final String message;


    public static CurrencyPairException unsupportedPair(CurrencyPair currencyPair) {
        var message = String.format("Unsupported currency pair [%s|%s], supported currencies are %s",
                currencyPair.getFrom(), currencyPair.getTo(), CurrencyPairValidator.SUPPORTED_CURRENCIES);

        return CurrencyPairException.builder()
                .withMessage(message)
                .build();
    }
}

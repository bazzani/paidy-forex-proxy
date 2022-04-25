package com.paidyinc.forex;

import com.paidyinc.forex.model.CurrencyPair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyPairValidator {
    static final List<String> SUPPORTED_CURRENCIES = List.of("AUD", "CAD", "CHF", "EUR", "GBP", "NZD", "JPY", "SGD", "USD");

    public boolean pairIsSupported(CurrencyPair pair) {
        var bothCurrenciesSupported = SUPPORTED_CURRENCIES.containsAll(List.of(pair.getFrom(), pair.getTo()));
        var currencyPairsDoNotMatch = !pair.getFrom().equalsIgnoreCase(pair.getTo());
        return bothCurrenciesSupported && currencyPairsDoNotMatch;
    }
}

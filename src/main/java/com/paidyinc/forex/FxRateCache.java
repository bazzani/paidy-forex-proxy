package com.paidyinc.forex;

import com.paidyinc.forex.model.CurrencyPair;
import com.paidyinc.forex.model.ForexResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
class FxRateCache {
    private static final Logger LOG = LoggerFactory.getLogger(FxRateCache.class);

    private final Map<String, ForexResult> rateCache = new HashMap<>();

    Optional<ForexResult> rateFromThePastFiveMinutes(CurrencyPair currencyPair) {
        String pairCacheKey = currencyPair.pairAsString();

        if (rateCache.containsKey(pairCacheKey) && rateIsLessThanFiveMinutesOld(rateCache.get(pairCacheKey))) {
            LOG.debug("Found current pair in cache [{}]", currencyPair);

            return Optional.of(rateCache.get(pairCacheKey));
        } else {
            LOG.debug("Current pair missing in cache [{}]", currencyPair);

            return Optional.empty();
        }
    }

    private boolean rateIsLessThanFiveMinutesOld(ForexResult forexResult) {
        return !forexResult.getTimestamp().isBefore(ZonedDateTime.now().withNano(0).minusMinutes(5));
    }

    void addRate(CurrencyPair currencyPair, ForexResult forexRate) {
        rateCache.put(currencyPair.pairAsString(), forexRate);
    }
}

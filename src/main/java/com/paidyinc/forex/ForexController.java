package com.paidyinc.forex;

import com.paidyinc.forex.model.CurrencyPair;
import com.paidyinc.forex.model.ForexResult;
import com.paidyinc.forex.service.FxRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForexController {
    private static final Logger LOG = LoggerFactory.getLogger(ForexController.class);

    private final CurrencyPairValidator currencyPairValidator;
    private final FxRateService fxRateService;
    private final FxRateCache fxRateCache;

    @Autowired
    public ForexController(CurrencyPairValidator currencyPairValidator, FxRateService fxRateService, FxRateCache fxRateCache) {
        this.currencyPairValidator = currencyPairValidator;
        this.fxRateService = fxRateService;
        this.fxRateCache = fxRateCache;
    }

    @GetMapping(value = "/fx/{from}/{to}")
    @ResponseBody
    public ForexResult getForexResult(@PathVariable("from") String fromCurrency, @PathVariable("to") String toCurrency) {
        LOG.debug("getting rate for FX Pair: [from {} >> to {}]", fromCurrency, toCurrency);

        var currencyPair = CurrencyPair.builder()
                .withFrom(fromCurrency)
                .withTo(toCurrency)
                .build();

        if (currencyPairValidator.pairIsSupported(currencyPair)) {
            return fxRateCache.rateFromThePastFiveMinutes(currencyPair)
                    .orElseGet(() -> getForexRateFromService(currencyPair));
        } else {
            throw CurrencyPairException.unsupportedPair(currencyPair);
        }
    }

    private ForexResult getForexRateFromService(CurrencyPair currencyPair) {
        var fxRateResponse = fxRateService.getFxRate(currencyPair);

        LOG.debug("  found rate for FX Pair [{} >> {}] = {}", currencyPair.getFrom(), currencyPair.getTo(), fxRateResponse.getRate());

        var forexRate = ForexResult.builder()
                .withRate(fxRateResponse.getRate())
                .withTimestamp(fxRateResponse.getTimestamp())
                .build();

        fxRateCache.addRate(currencyPair, forexRate);

        return forexRate;
    }
}

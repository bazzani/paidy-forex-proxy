package com.paidyinc.forex.service;

import com.paidyinc.forex.model.CurrencyPair;

/**
 * Service to get FX Rates from a backing third party API, hiding the implementation.
 */
public interface FxRateService {

    /**
     * Get the FX Rate for a currency pair
     *
     * @param currencyPair the pair of currencies to get the exchange rate for
     * @return an {@link FxRateResponse} with the exchange rate and the time it was acquired from the backing API
     */
    FxRateResponse getFxRate(CurrencyPair currencyPair);
}

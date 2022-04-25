package com.paidyinc.forex.service.local;

import com.paidyinc.forex.model.CurrencyPair;
import com.paidyinc.forex.service.FxRateResponse;
import com.paidyinc.forex.service.FxRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Implementation of the @{@link FxRateService} for LOCAL DEVELOPMENT which fetches exchange rates between pairs of supported currencies.<p/>
 * The rate and the timestamp will contain hardcoded values and be eturned in a @{@link FxRateResponse} POJO.
 */
@Service
public class LocalMockFxRateService implements FxRateService {
    private static final Logger LOG = LoggerFactory.getLogger(LocalMockFxRateService.class);

    @Override
    public FxRateResponse getFxRate(CurrencyPair currencyPair) {

        LOG.debug("Getting FX Rate for currency Pair[{}] from local mock service", currencyPair);

        var nowDate = LocalDate.now();
        var nowTime = LocalTime.now().withNano(0);
        var now = ZonedDateTime.of(nowDate, nowTime, ZoneId.systemDefault());
        return FxRateResponse.builder()
                .withRate(123.45d)
                .withTimestamp(now)
                .build();
    }
}

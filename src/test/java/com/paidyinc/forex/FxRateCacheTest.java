package com.paidyinc.forex;

import com.paidyinc.forex.model.CurrencyPair;
import com.paidyinc.forex.model.ForexResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FxRateCacheTest {
    FxRateCache sut;

    @BeforeEach
    void setup() {
        sut = new FxRateCache();
    }

    @Test
    void shouldGetRateFromWithinPastFiveMinutes() {
        // given
        var pair = getDefaultCurrencyPair();
        var forexResultFromTwoMinutesAgo = getForexResultFromTwoMinutesAgo();
        sut.addRate(pair, forexResultFromTwoMinutesAgo);

        // when
        var forexResultOptional = sut.rateFromThePastFiveMinutes(pair);

        // then
        assertThat(forexResultOptional.isPresent(), is(true));
        assertThat(forexResultOptional.get().getRate(), is(123.45d));
        assertThat(forexResultOptional.get().getTimestamp(), is(forexResultFromTwoMinutesAgo.getTimestamp()));
    }

    @Test
    void shouldNotGetRateWhenCurrencyPairNotInCache() {
        // given
        var pair = getDefaultCurrencyPair();

        // when
        var forexResultOptional = sut.rateFromThePastFiveMinutes(pair);

        // then
        assertThat(forexResultOptional.isPresent(), is(false));
    }

    @Test
    void shouldNotGetRateFromOutsidePastFiveMinutes() {
        // given
        var pair = getDefaultCurrencyPair();
        var forexResult = getForexResultFromSixMinutesAgo();
        sut.addRate(pair, forexResult);

        // when
        var forexResultOptional = sut.rateFromThePastFiveMinutes(pair);

        // then
        assertThat(forexResultOptional.isPresent(), is(false));
    }

    private CurrencyPair getDefaultCurrencyPair() {
        return CurrencyPair.builder()
                .withFrom("USD")
                .withTo("EUR")
                .build();
    }

    private ForexResult getForexResultFromTwoMinutesAgo() {
        return getForexResultMinusMinutes(2);
    }

    private ForexResult getForexResultFromSixMinutesAgo() {
        return getForexResultMinusMinutes(6);
    }

    private ForexResult getForexResultMinusMinutes(int minusMinutes) {
        ZonedDateTime timestamp = ZonedDateTime.now().withNano(0).minusMinutes(minusMinutes);

        return ForexResult.builder()
                .withRate(123.45d)
                .withTimestamp(timestamp)
                .build();
    }
}

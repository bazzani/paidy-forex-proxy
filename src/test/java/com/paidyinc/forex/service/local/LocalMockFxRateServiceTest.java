package com.paidyinc.forex.service.local;

import com.paidyinc.forex.model.CurrencyPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class LocalMockFxRateServiceTest {

    private LocalMockFxRateService sut;

    @BeforeEach
    void setup() {
        sut = new LocalMockFxRateService();
    }

    @Test
    void shouldGetMockedFxRate() {
        // given
        var pair = CurrencyPair.builder()
                .withFrom("EUR")
                .withTo("USD")
                .build();

        // when
        var fxRate = sut.getFxRate(pair);

        // then
        assertThat(fxRate.getRate(), is(123.45d));

        var nowDate = LocalDate.now();
        var nowTime = LocalTime.now().withNano(0);
        var now = ZonedDateTime.of(nowDate, nowTime, ZoneId.systemDefault());

        assertThat(fxRate.getTimestamp(), is(now));
    }
}

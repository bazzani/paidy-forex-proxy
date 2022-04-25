package com.paidyinc.forex;

import com.paidyinc.forex.model.CurrencyPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.paidyinc.forex.CurrencyPairValidator.SUPPORTED_CURRENCIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CurrencyPairValidatorTest {
    private CurrencyPairValidator sut;

    @BeforeEach
    void setup() {
        sut = new CurrencyPairValidator();
    }

    @ParameterizedTest
    @MethodSource("getValidCurrencyPairs")
    void shouldSupportValidCurrencyPair(String fromCurrency, String toCurrency) {
        // given
        var pair = CurrencyPair.builder()
                .withFrom(fromCurrency)
                .withTo(toCurrency)
                .build();

        // when
        boolean isValidPair = sut.pairIsSupported(pair);

        // then
        assertThat(isValidPair, is(true));

    }

    @ParameterizedTest
    @MethodSource("getInvalidMatchingCurrencyPairs")
    void shouldNotSupportIdenticalCurrencyPair(String fromCurrency, String toCurrency) {
        // given
        var pair = CurrencyPair.builder()
                .withFrom(fromCurrency)
                .withTo(toCurrency)
                .build();

        // when
        boolean isValidPair = sut.pairIsSupported(pair);

        // then
        assertThat(isValidPair, is(false));
    }

    @Test
    void shouldNotSupportUnknownFromCurrency() {
        // given
        var pair = CurrencyPair.builder()
                .withFrom("foo")
                .withTo("USD")
                .build();

        // when
        boolean isValidPair = sut.pairIsSupported(pair);

        // then
        assertThat(isValidPair, is(false));
    }

    @Test
    void shouldNotSupportUnknownToCurrency() {
        // given
        var pair = CurrencyPair.builder()
                .withFrom("USD")
                .withTo("foo")
                .build();

        // when
        boolean isValidPair = sut.pairIsSupported(pair);

        // then
        assertThat(isValidPair, is(false));
    }

    private static Stream<Arguments> getValidCurrencyPairs() {
        List<Arguments> currencyPairs = new ArrayList<>();

        SUPPORTED_CURRENCIES.forEach(fromCurrency -> SUPPORTED_CURRENCIES.forEach(toCurrency -> {
                    boolean pairIsDistinct = !fromCurrency.equalsIgnoreCase(toCurrency);
                    if (pairIsDistinct) {
                        currencyPairs.add(Arguments.of(fromCurrency, toCurrency));
                    }
                }
        ));

        return currencyPairs.stream();
    }

    private static Stream<Arguments> getInvalidMatchingCurrencyPairs() {
        List<Arguments> currencyPairs = new ArrayList<>();

        SUPPORTED_CURRENCIES.forEach(fromCurrency -> SUPPORTED_CURRENCIES.forEach(toCurrency -> {
                    boolean pairsMatch = fromCurrency.equalsIgnoreCase(toCurrency);
                    if (pairsMatch) {
                        currencyPairs.add(Arguments.of(fromCurrency, toCurrency));
                    }
                }
        ));

        return currencyPairs.stream();
    }
}

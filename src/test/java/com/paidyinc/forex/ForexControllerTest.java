package com.paidyinc.forex;

import com.paidyinc.forex.model.CurrencyPair;
import com.paidyinc.forex.service.FxRateResponse;
import com.paidyinc.forex.service.FxRateService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForexController.class)
class ForexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CurrencyPairValidator mockCurrencyPairValidator;

    @MockBean
    FxRateService mockFxRateService;

    @MockBean
    FxRateCache mockFxRateCache;

    @Test
    @SneakyThrows
    void shouldGet200ResponseWithFXRateFromUsdToEur() {
        // given
        var pair = CurrencyPair.builder()
                .withFrom("USD")
                .withTo("EUR")
                .build();
        given(mockCurrencyPairValidator.pairIsSupported(currencyPairArgumentMatches(pair))).willReturn(true);

        given(mockFxRateCache.rateFromThePastFiveMinutes(currencyPairArgumentMatches(pair))).willReturn(Optional.empty());

        var fxRateResponse = FxRateResponse.builder()
                .withRate(123.45d)
                .withTimestamp(ZonedDateTime.of(LocalDateTime.of(2022, Month.APRIL, 26, 12, 34), ZoneId.systemDefault()))
                .build();
        given(mockFxRateService.getFxRate(currencyPairArgumentMatches(pair))).willReturn(fxRateResponse);

        // when
        var resultActions = this.mockMvc
                .perform(get("/fx/USD/EUR"))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(123.45));
    }

    private CurrencyPair currencyPairArgumentMatches(CurrencyPair pair) {
        return argThat(currencyPair ->
                currencyPair.getFrom().equalsIgnoreCase(pair.getFrom()) && currencyPair.getTo().equalsIgnoreCase(pair.getTo())
        );
    }

    @Test
    @SneakyThrows
    void shouldGet400ResponseWithErrorMessageFromUsdToFooPair() {
        // given
        var pair = CurrencyPair.builder()
                .withFrom("USD")
                .withTo("Foo")
                .build();
        given(mockCurrencyPairValidator.pairIsSupported(pair)).willReturn(false);
        var expectedErrorMessage = "Unsupported currency pair [USD|Foo], supported currencies are [AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD]";

        // when
        var resultActions = this.mockMvc.perform(get("/fx/USD/Foo"))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }
}

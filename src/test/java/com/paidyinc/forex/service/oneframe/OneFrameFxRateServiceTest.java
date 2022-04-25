package com.paidyinc.forex.service.oneframe;

import com.paidyinc.forex.CurrencyPairException;
import com.paidyinc.forex.model.CurrencyPair;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OneFrameFxRateServiceTest {
    private OneFrameFxRateService sut;

    private MockWebServer mockWebServer;

    @SneakyThrows
    @BeforeEach
    void setup() {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        var rootUrl = mockWebServer.url("").toString();
        sut = new OneFrameFxRateService(WebClient.create(rootUrl));
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @SneakyThrows
    void shouldGetFxRateForEURtoUSD() {
        // given
        var pair = getCurrencyPair();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("[{\"from\":\"EUR\",\"to\":\"USD\",\"bid\":123.45,\"ask\":345.67,\"price\":234.56,\"time_stamp\":\"2022-04-27T10:59:40.22Z\"}]");
        mockWebServer.enqueue(response);

        // when
        var fxRate = sut.getFxRate(pair);

        // then
        var request = mockWebServer.takeRequest();
        assertThat(request.getMethod(), is("GET"));
        assertThat(request.getPath(), is("/rates?pair=EURUSD"));

        assertThat(fxRate.getRate(), is(234.56));
    }

    @Test
    @SneakyThrows
    void shouldGetErrorWhenErrorMessageReceivedFromOneFrameAPI() {
        // given
        var pair = getCurrencyPair();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("{\"error\":\"Quota reached\"}]");
        mockWebServer.enqueue(response);

        // when
        var exception = assertThrows(CurrencyPairException.class, () -> sut.getFxRate(pair));

        // then
        mockWebServer.takeRequest();
        assertThat(exception.getMessage(), is("Error fetching currency pair from One-frame: Quota reached"));
    }

    @Test
    @SneakyThrows
    void shouldGetErrorWhenInvalidResponseReceivedFromOneFrameAPI() {
        // given
        var pair = getCurrencyPair();

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("123");
        mockWebServer.enqueue(response);

        // when
        var exception = assertThrows(CurrencyPairException.class, () -> sut.getFxRate(pair));

        // then
        mockWebServer.takeRequest();
        assertThat(exception.getMessage(), is("Error fetching currency pair from One-frame: Invalid response received from One-frame"));
    }

    private CurrencyPair getCurrencyPair() {
        return CurrencyPair.builder()
                .withFrom("EUR")
                .withTo("USD")
                .build();
    }
}

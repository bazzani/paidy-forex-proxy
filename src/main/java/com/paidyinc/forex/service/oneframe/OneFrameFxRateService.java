package com.paidyinc.forex.service.oneframe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paidyinc.forex.CurrencyPairException;
import com.paidyinc.forex.model.CurrencyPair;
import com.paidyinc.forex.service.FxRateResponse;
import com.paidyinc.forex.service.FxRateService;
import com.paidyinc.forex.service.oneframe.model.OneFrameRate;
import com.paidyinc.forex.service.oneframe.model.OneFrameResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Implementation of the @{@link FxRateService} which fetches exchange rates between pairs of supported currencies.<p/>
 * The rate and the timestamp of when it was retrieved is returned in a @{@link FxRateResponse} POJO.
 */
@Service
@Profile("oneframe")
@Primary
public class OneFrameFxRateService implements FxRateService {
    private static final Logger LOG = LoggerFactory.getLogger(OneFrameFxRateService.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    private final WebClient webClient;

    @Autowired
    public OneFrameFxRateService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public FxRateResponse getFxRate(CurrencyPair currencyPair) {
        LOG.debug("Getting FX Rate for currency Pair from One-frame [{}]", currencyPair);

        var oneFrameResponse = getResponseFromWebClient(currencyPair);

        if (oneFrameResponse.isErrored()) {
            throw CurrencyPairException.builder()
                    .withMessage("Error fetching currency pair from One-frame: " + oneFrameResponse.getError())
                    .build();
        }

        return getFxRateFromOneFrameResponse(oneFrameResponse.getRate());
    }

    private OneFrameResponse getResponseFromWebClient(CurrencyPair currencyPair) {
        var currencies = currencyPair.getFrom() + currencyPair.getTo();
        var response = webClient.get()
                .uri("rates?pair={pair}", currencies)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        if (response instanceof List) {
            return getResponseFromList((List<LinkedHashMap<String, String>>) response);
        } else if (response instanceof LinkedHashMap) {
            return getResponseFromError((LinkedHashMap<String, String>) response);
        } else {
            LOG.error("Invalid response received from One-frame: [{}]", response);

            return OneFrameResponse.builder()
                    .withError("Invalid response received from One-frame")
                    .build();
        }
    }

    private OneFrameResponse getResponseFromList(List<LinkedHashMap<String, String>> response) {
        var firstRateMap = response.get(0);
        var oneFrameRate = MAPPER.convertValue(firstRateMap, OneFrameRate.class);

        return OneFrameResponse.builder()
                .withRate(oneFrameRate)
                .build();
    }

    private OneFrameResponse getResponseFromError(LinkedHashMap<String, String> errorResponseMap) {
        var error = errorResponseMap.get("error");

        return OneFrameResponse.builder()
                .withError(error)
                .build();
    }

    private FxRateResponse getFxRateFromOneFrameResponse(OneFrameRate oneFrameRate) {
        return FxRateResponse.builder()
                .withRate(oneFrameRate.getPrice())
                .withTimestamp(oneFrameRate.getTime_stamp())
                .build();
    }
}

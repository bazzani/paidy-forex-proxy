package com.paidyinc.forex.service;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder(setterPrefix = "with")
public class FxRateResponse {
    private final double rate;
    private final ZonedDateTime timestamp;
}

package com.paidyinc.forex.model;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder(setterPrefix = "with")
public class ForexResult {
    private final double rate;
    private final ZonedDateTime timestamp;
}

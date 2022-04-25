package com.paidyinc.forex.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(setterPrefix = "with")
@ToString
public class CurrencyPair {
    private final String from;
    private final String to;

    public String pairAsString() {
        return from + to;
    }
}

package com.paidyinc.forex;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForexResult {
    private final double rate;
}

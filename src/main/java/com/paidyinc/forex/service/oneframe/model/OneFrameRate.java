package com.paidyinc.forex.service.oneframe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * The Response POJO representing a currency pair rate returned from the Paidy one-frame API.
 */
@Getter
@NoArgsConstructor
public class OneFrameRate {
    private String from;
    private String to;
    private double bid;
    private double ask;
    private double price;
    private ZonedDateTime time_stamp;
}

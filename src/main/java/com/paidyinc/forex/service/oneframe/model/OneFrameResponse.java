package com.paidyinc.forex.service.oneframe.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * The Response POJO which is returned from the Paidy one-frame API. The response will either have a {@link OneFrameRate},
 * or an error message property, not both.
 */
@Getter
@Builder(setterPrefix = "with")
public class OneFrameResponse {
    private String error;
    private OneFrameRate rate;

    public boolean isErrored() {
        return StringUtils.hasLength(error);
    }
}

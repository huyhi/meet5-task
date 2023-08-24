package com.hongye.model;

import lombok.Builder;
import lombok.Data;

/**
 * ResultData used for define HTTP API response structure
 */
@Data
@Builder
public class ResponseData {

    public boolean respStatus;

    public String message;

    public static ResponseData success() {
        return ResponseData.builder().respStatus(true).build();
    }

    public static ResponseData error(String errMsg) {
        return ResponseData.builder()
                .respStatus(false)
                .message(errMsg)
                .build();
    }
}

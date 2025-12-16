package com.cathay.coindesk.model.dto;

import java.util.Map;
import lombok.Data;

@Data
public class CoindeskResponse {

    private TimeInfo time;
    private String disclaimer;
    private String chartName;
    private Map<String, CurrencyInfo> bpi;

    @Data
    public static class TimeInfo {
        private String updated;
        private String updatedISO;
        private String updateduk;
    }

    @Data
    public static class CurrencyInfo {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        private float rate_float;
    }
}

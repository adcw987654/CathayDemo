package com.cathay.coindesk.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoindeskTransformedResponse {

    private String updatedTime;
    private List<CurrencyDetail> details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrencyDetail {
        private String code;
        private String chineseName;
        private String rate;
    }
}

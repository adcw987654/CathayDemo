package com.cathay.coindesk.service;

import com.cathay.coindesk.model.dto.CoindeskResponse;
import com.cathay.coindesk.model.dto.CoindeskResponse.CurrencyInfo;
import com.cathay.coindesk.model.dto.CoindeskTransformedResponse;
import com.cathay.coindesk.model.dto.CoindeskTransformedResponse.CurrencyDetail;
import com.cathay.coindesk.model.entity.Currency;
import com.cathay.coindesk.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CoindeskService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyRepository currencyRepository;

    private static final String COINDESK_URL = "https://kengp3.github.io/blog/coindesk.json";

    public CoindeskResponse callCoindeskApi() {
        log.info("Calling Coindesk API: {}", COINDESK_URL);
        return restTemplate.getForObject(COINDESK_URL, CoindeskResponse.class);
    }

    public CoindeskTransformedResponse getTransformedData() {
        log.info("Fetching and transforming Coindesk data");
        CoindeskResponse origin = callCoindeskApi();

        String isoTime = origin.getTime().getUpdatedISO();
        String formattedTime = convertDate(isoTime);

        List<CurrencyDetail> details = new ArrayList<>();
        Map<String, CurrencyInfo> bpi = origin.getBpi();

        if (bpi != null) {
            for (Map.Entry<String, CurrencyInfo> entry : bpi.entrySet()) {
                String code = entry.getKey();
                CurrencyInfo info = entry.getValue();

                Optional<Currency> currency = currencyRepository.findByCode(code);
                String chineseName = currency.map(Currency::getChineseName).orElse("N/A");

                details.add(new CurrencyDetail(
                        code, chineseName, info.getRate()));
            }
        }

        log.info("Transformation complete, processed {} currencies", details.size());
        return new CoindeskTransformedResponse(formattedTime, details);
    }

    private String convertDate(String isoDate) {
        try {
            OffsetDateTime odt = OffsetDateTime.parse(isoDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            return odt.format(formatter);
        } catch (Exception e) {
            log.error("Error parsing date: {}", isoDate, e);
            return isoDate; // Fallback
        }
    }
}

package com.cathay.coindesk.service;

import com.cathay.coindesk.model.dto.CoindeskResponse;
import com.cathay.coindesk.model.dto.CoindeskTransformedResponse;
import com.cathay.coindesk.model.entity.Currency;
import com.cathay.coindesk.repository.CurrencyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CoindeskServiceTest {

    @InjectMocks
    private CoindeskService coindeskService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    public void testGetTransformedData() {
        // Arrange
        CoindeskResponse mockResponse = new CoindeskResponse();
        CoindeskResponse.TimeInfo timeInfo = new CoindeskResponse.TimeInfo();
        timeInfo.setUpdatedISO("2024-09-02T07:07:20+00:00");
        mockResponse.setTime(timeInfo);

        Map<String, CoindeskResponse.CurrencyInfo> bpi = new HashMap<>();
        CoindeskResponse.CurrencyInfo usdInfo = new CoindeskResponse.CurrencyInfo();
        usdInfo.setCode("USD");
        usdInfo.setRate("57,756.298");
        bpi.put("USD", usdInfo);

        CoindeskResponse.CurrencyInfo gbpInfo = new CoindeskResponse.CurrencyInfo();
        gbpInfo.setCode("GBP");
        gbpInfo.setRate("43,984.02");
        bpi.put("GBP", gbpInfo);

        mockResponse.setBpi(bpi);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(CoindeskResponse.class)))
                .thenReturn(mockResponse);

        Mockito.when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(new Currency("USD", "美元")));
        Mockito.when(currencyRepository.findByCode("GBP")).thenReturn(Optional.of(new Currency("GBP", "英鎊")));

        // Act
        CoindeskTransformedResponse result = coindeskService.getTransformedData();

        // Assert
        Assertions.assertEquals("2024/09/02 07:07:20", result.getUpdatedTime());
        Assertions.assertEquals(2, result.getDetails().size());

        // Check USD
        CoindeskTransformedResponse.CurrencyDetail usdDetail = result.getDetails().stream()
                .filter(d -> d.getCode().equals("USD")).findFirst().orElse(null);
        Assertions.assertNotNull(usdDetail);
        Assertions.assertEquals("美元", usdDetail.getChineseName());
        Assertions.assertEquals("57,756.298", usdDetail.getRate());
    }
}

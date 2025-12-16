package com.cathay.coindesk;

import com.cathay.coindesk.model.dto.CoindeskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CoindeskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        // Prepare Mock Data from Coindesk
        CoindeskResponse mockResponse = new CoindeskResponse();
        CoindeskResponse.TimeInfo timeInfo = new CoindeskResponse.TimeInfo();
        timeInfo.setUpdatedISO("2024-09-02T07:07:20+00:00");
        mockResponse.setTime(timeInfo);

        Map<String, CoindeskResponse.CurrencyInfo> bpi = new HashMap<>();
        CoindeskResponse.CurrencyInfo usdInfo = new CoindeskResponse.CurrencyInfo();
        usdInfo.setCode("USD");
        usdInfo.setRate("57,756.298");
        bpi.put("USD", usdInfo);

        mockResponse.setBpi(bpi);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(CoindeskResponse.class)))
                .thenReturn(mockResponse);
    }

    @Test
    public void testCallOriginalApi() throws Exception {
        mockMvc.perform(get("/api/coindesk/original")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testCallTransformedApi() throws Exception {
        mockMvc.perform(get("/api/coindesk/transform")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.updatedTime").exists())
                .andExpect(jsonPath("$.data.details").isArray());
    }
}

package com.cathay.coindesk.controller;

import com.cathay.coindesk.mapper.CurrencyMapper;
import com.cathay.coindesk.model.bo.CurrencyBo;
import com.cathay.coindesk.model.dto.CurrencyDto;
import com.cathay.coindesk.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    private CurrencyMapper currencyMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCurrencies() throws Exception {
        CurrencyBo bo1 = new CurrencyBo(1L, "USD", "美元");
        CurrencyBo bo2 = new CurrencyBo(2L, "GBP", "英鎊");
        CurrencyDto dto1 = new CurrencyDto("USD", "美元");
        CurrencyDto dto2 = new CurrencyDto("GBP", "英鎊");

        Mockito.when(currencyService.getAllCurrencies()).thenReturn(Arrays.asList(bo1, bo2));
        Mockito.when(currencyMapper.toDto(bo1)).thenReturn(dto1);
        Mockito.when(currencyMapper.toDto(bo2)).thenReturn(dto2);

        mockMvc.perform(get("/api/currency"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].code").value("USD"))
                .andExpect(jsonPath("$.data[1].chineseName").value("英鎊"));
    }

    @Test
    public void testCreateCurrency() throws Exception {
        CurrencyDto inputDto = new CurrencyDto("JPY", "日圓");
        CurrencyBo inputBo = new CurrencyBo(null, "JPY", "日圓");
        CurrencyBo outputBo = new CurrencyBo(3L, "JPY", "日圓");
        CurrencyDto outputDto = new CurrencyDto("JPY", "日圓");

        Mockito.when(currencyMapper.toBo(any(CurrencyDto.class))).thenReturn(inputBo);
        Mockito.when(currencyService.createCurrency(any(CurrencyBo.class))).thenReturn(outputBo);
        Mockito.when(currencyMapper.toDto(any(CurrencyBo.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/currency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.code").value("JPY"));
    }

    @Test
    public void testUpdateCurrency() throws Exception {
        CurrencyDto inputDto = new CurrencyDto("USD", "美金");
        CurrencyBo inputBo = new CurrencyBo(null, "USD", "美金");
        CurrencyBo outputBo = new CurrencyBo(1L, "USD", "美金");
        CurrencyDto outputDto = new CurrencyDto("USD", "美金");

        Mockito.when(currencyMapper.toBo(any(CurrencyDto.class))).thenReturn(inputBo);
        Mockito.when(currencyService.updateCurrency(eq("USD"), any(CurrencyBo.class))).thenReturn(outputBo);
        Mockito.when(currencyMapper.toDto(any(CurrencyBo.class))).thenReturn(outputDto);

        mockMvc.perform(put("/api/currency/USD")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.chineseName").value("美金"));
    }

    @Test
    public void testDeleteCurrency() throws Exception {
        Mockito.doNothing().when(currencyService).deleteCurrency("USD");

        mockMvc.perform(delete("/api/currency/USD"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testGetCurrencyNotFound() throws Exception {
        Mockito.when(currencyService.getCurrencyByCode("ZZZ")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/currency/ZZZ"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }
}

package com.cathay.coindesk.controller;

import com.cathay.coindesk.mapper.CurrencyMapper;
import com.cathay.coindesk.model.bo.CurrencyBo;
import com.cathay.coindesk.model.dto.ApiResponse;
import com.cathay.coindesk.model.dto.CurrencyDto;
import com.cathay.coindesk.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyMapper currencyMapper;

    @GetMapping
    public ApiResponse<List<CurrencyDto>> getAllCurrencies() {
        log.info("Received request to get all currencies");
        List<CurrencyDto> dtos = currencyService.getAllCurrencies().stream()
                .map(currencyMapper::toDto)
                .collect(Collectors.toList());
        return ApiResponse.success(dtos);
    }

    @GetMapping("/{code}")
    public ApiResponse<CurrencyDto> getCurrencyByCode(@PathVariable String code) {
        log.info("Received request to get currency: {}", code);
        return currencyService.getCurrencyByCode(code)
                .map(currencyMapper::toDto)
                .map(ApiResponse::success)
                .orElseThrow(() -> new RuntimeException("Currency not found for code: " + code));
    }

    @PostMapping
    public ApiResponse<CurrencyDto> createCurrency(@RequestBody CurrencyDto currencyDto) {
        log.info("Received request to create currency: {}", currencyDto.getCode());
        CurrencyBo bo = currencyMapper.toBo(currencyDto);
        CurrencyBo savedBo = currencyService.createCurrency(bo);
        return ApiResponse.success(currencyMapper.toDto(savedBo));
    }

    @PutMapping("/{code}")
    public ApiResponse<CurrencyDto> updateCurrency(@PathVariable String code, @RequestBody CurrencyDto currencyDto) {
        log.info("Received request to update currency: {}", code);
        CurrencyBo bo = currencyMapper.toBo(currencyDto);
        CurrencyBo updatedBo = currencyService.updateCurrency(code, bo);
        return ApiResponse.success(currencyMapper.toDto(updatedBo));
    }

    @DeleteMapping("/{code}")
    public ApiResponse<Void> deleteCurrency(@PathVariable String code) {
        log.info("Received request to delete currency: {}", code);
        currencyService.deleteCurrency(code);
        return ApiResponse.success();
    }
}

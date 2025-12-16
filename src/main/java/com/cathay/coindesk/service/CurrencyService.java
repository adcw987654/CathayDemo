package com.cathay.coindesk.service;

import com.cathay.coindesk.mapper.CurrencyMapper;
import com.cathay.coindesk.model.bo.CurrencyBo;
import com.cathay.coindesk.model.entity.Currency;
import com.cathay.coindesk.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    public List<CurrencyBo> getAllCurrencies() {
        log.info("Fetching all currencies");
        return currencyRepository.findAll().stream()
                .map(currencyMapper::toBo)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyBo> getCurrencyByCode(String code) {
        log.info("Fetching currency with code: {}", code);
        return currencyRepository.findByCode(code)
                .map(currencyMapper::toBo);
    }

    @Transactional
    public CurrencyBo createCurrency(CurrencyBo currencyBo) {
        log.info("Creating currency: {} ({})", currencyBo.getCode(), currencyBo.getChineseName());
        if (currencyRepository.findByCode(currencyBo.getCode()).isPresent()) {
            throw new RuntimeException("Currency code already exists: " + currencyBo.getCode());
        }
        Currency currency = currencyMapper.toEntity(currencyBo);
        Currency saved = currencyRepository.save(currency);
        return currencyMapper.toBo(saved);
    }

    @Transactional
    public CurrencyBo updateCurrency(String code, CurrencyBo currencyBo) {
        log.info("Updating currency with code: {}", code);
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Currency not found for code: {}", code);
                    return new RuntimeException("Currency not found for code :: " + code);
                });

        currency.setChineseName(currencyBo.getChineseName());
        Currency updated = currencyRepository.save(currency);
        return currencyMapper.toBo(updated);
    }

    @Transactional
    public void deleteCurrency(String code) {
        log.info("Deleting currency with code: {}", code);
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Currency not found for code: {}", code);
                    return new RuntimeException("Currency not found for code :: " + code);
                });
        currencyRepository.delete(currency);
    }
}

package com.cathay.coindesk.controller;

import com.cathay.coindesk.model.dto.ApiResponse;
import com.cathay.coindesk.model.dto.CoindeskResponse;
import com.cathay.coindesk.model.dto.CoindeskTransformedResponse;
import com.cathay.coindesk.service.CoindeskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/coindesk")
public class CoindeskController {

    @Autowired
    private CoindeskService coindeskService;

    @GetMapping("/original")
    public ApiResponse<CoindeskResponse> getOriginalData() {
        log.info("Received request for original Coindesk data");
        return ApiResponse.success(coindeskService.callCoindeskApi());
    }

    @GetMapping("/transform")
    public ApiResponse<CoindeskTransformedResponse> getTransformedData() {
        log.info("Received request for transformed Coindesk data");
        return ApiResponse.success(coindeskService.getTransformedData());
    }
}

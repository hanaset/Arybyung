package com.how.arybyungprovider.web.rest;

import com.how.arybyungprovider.service.ProviderEsService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/unit_test")
public class UnitRestTest extends ProviderRestSupport {

    private final ProviderEsService providerEsService;

    public UnitRestTest(ProviderEsService providerEsService) {
        this.providerEsService = providerEsService;
    }

    @GetMapping("/delete")
    public ResponseEntity deleteTest() {
        providerEsService.deleteBeforeWeekData();
        return response("ok");
    }
}

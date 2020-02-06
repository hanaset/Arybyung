package com.how.arybyungprovider.web.rest;

import com.how.arybyungprovider.service.ProviderEsService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/unit_test")
@Api(tags = "HowMuch Unit Test API")
public class UnitRestTest extends ProviderRestSupport {

    private final ProviderEsService providerEsService;

    public UnitRestTest(ProviderEsService providerEsService) {
        this.providerEsService = providerEsService;
    }

    @ApiOperation(
            value = "1주일 이상의 데이터 삭제 TEST"
    )
    @GetMapping("/delete")
    public ResponseEntity deleteTest() {
        providerEsService.deleteBeforeWeekData();
        return response("ok");
    }

    @ApiOperation(
            value = "ES에서 직접 검색 TEST"
    )
    @GetMapping("/search")
    public ResponseEntity searchTest(String keyword) {
        return response(providerEsService.searchKeyword(keyword));
    }
}

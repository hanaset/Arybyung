package com.how.arybyungprovider.web.rest;

import com.how.arybyungprovider.service.ProviderSearchService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/keyword")
@Api(tags = "HowMuch Search Service API")
public class ProviderSearchRest extends ProviderRestSupport {

    private final ProviderSearchService providerSearchService;

    public ProviderSearchRest(ProviderSearchService providerSearchService) {
        this.providerSearchService = providerSearchService;
    }

    @ApiOperation(
            value = "HowMuch 중고 제품 조회"
    )
    @GetMapping("/search")
    public ResponseEntity keywordSearch(String keyword) {
        return response(providerSearchService.searchKeyword(keyword));
    }
}

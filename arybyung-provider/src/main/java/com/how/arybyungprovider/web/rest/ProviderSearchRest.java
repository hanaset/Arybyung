package com.how.arybyungprovider.web.rest;

import com.how.arybyungprovider.service.PopularRankingService;
import com.how.arybyungprovider.service.ProviderSearchService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/keyword")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "HowMuch Search Service API")
public class ProviderSearchRest extends ProviderRestSupport {

    private final ProviderSearchService providerSearchService;
    private final PopularRankingService popularRankingService;

    @ApiOperation(
            value = "HowMuch 중고 제품 조회"
    )
    @GetMapping("/search")
    public ResponseEntity keywordSearch(String keyword) {
        return response(providerSearchService.basicSearchKeyword(keyword));
    }

    @ApiOperation(
            value = "PriceHero 인기검색차트 "
    )
    @GetMapping("/popular")
    public ResponseEntity popularChart() {
        return response(popularRankingService.getPopularChart());
    }
}

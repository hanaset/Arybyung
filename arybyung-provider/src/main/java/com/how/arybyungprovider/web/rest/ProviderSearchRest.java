package com.how.arybyungprovider.web.rest;

import com.how.arybyungprovider.model.kakao.request.KakaoRequest;
import com.how.arybyungprovider.model.kakao.response.KakaoResponse;
import com.how.arybyungprovider.service.ProviderSearchService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("v1/keyword")
@Slf4j
@Api(tags = "HowMuch Search Service API")
public class ProviderSearchRest extends ProviderRestSupport {

    private final ProviderSearchService providerSearchService;

    public ProviderSearchRest(ProviderSearchService providerSearchService) {
        this.providerSearchService = providerSearchService;
    }

    @ApiOperation(
            value = "HowMuch 중고 제품 조회(오픈빌더)"
    )
    @PostMapping("/search")
    public <T> KakaoResponse<?> keywordSearch(@RequestBody KakaoRequest request) {
        Map<String, String> keywordMap = request.getAction().getParams();

        if (keywordMap.containsKey("keyword")) {
            log.info(keywordMap.get("keyword"));
            return providerSearchService.kakaoSearchKeyword(keywordMap.get("keyword"));
        } else {
            return KakaoResponse.builder()
                    .version("1.0")
                    .template(null)
                    .build();
        }

    }

    @ApiOperation(
            value = "HowMuch 중고 제품 조회(default)"
    )
    @GetMapping("/test")
    public ResponseEntity test(String keyword) {
        return response(providerSearchService.basicSearchKeyword(keyword));
    }
}

package com.how.arybyungprovider.web.rest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.how.arybyungprovider.model.kakao.request.KakaoRequest;
import com.how.arybyungprovider.model.kakao.response.KakaoResponse;
import com.how.arybyungprovider.model.kakao.response.KakaoResponseTemplate;
import com.how.arybyungprovider.service.ProviderSearchService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            value = "HowMuch 중고 제품 조회"
    )
    @GetMapping("/search")
    public KakaoResponse keywordSearch(String keyword) {
        return providerSearchService.searchKeyword(keyword);
    }

    @PostMapping("/test")
    public KakaoResponse test(@RequestBody KakaoRequest request) {
        log.info("KakaoRequest => {}", request.toString());
        List<Map<String, Map<String, String>>> outputs = Lists.newArrayList();
        Map<String, String> innerMap = Maps.newHashMap();
        Map<String, Map<String, String>> outerMap = Maps.newHashMap();

        innerMap.put("text", "Test 입니다.");
        outerMap.put("simpleText", innerMap);
        outputs.add(outerMap);

        return KakaoResponse.builder()
                .version("v1.0")
                .template(KakaoResponseTemplate.builder()
                        .outputs(outputs)
                        .build())
                .build();
    }
}

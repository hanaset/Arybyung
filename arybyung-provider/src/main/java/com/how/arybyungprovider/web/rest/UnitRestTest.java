package com.how.arybyungprovider.web.rest;

import com.how.arybyungprovider.service.ProviderEsService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import com.how.muchcommon.model.type.FieldParam;
import com.how.muchcommon.model.type.OrderType;
import com.how.muchcommon.repository.elasticrepository.ArticleEsRepository;
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
    private final ArticleEsRepository articleEsRepository;

    public UnitRestTest(ProviderEsService providerEsService,
                        ArticleEsRepository articleEsRepository) {
        this.providerEsService = providerEsService;
        this.articleEsRepository = articleEsRepository;
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
    public ResponseEntity searchTest(String keyword, FieldParam field, OrderType order) {
        return response(providerEsService.searchKeyword(keyword, field, order));
    }

    @ApiOperation(
            value = "ES 전체 데이터"
    )
    @GetMapping("/findAll")
    public ResponseEntity findAllTest() {
        return response(articleEsRepository.findAll());
    }
}

package com.how.arybyungprovider.web.rest;

import com.how.arybyungprovider.service.ProviderEsService;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/keyword")
public class ProviderSearchRest extends ProviderRestSupport {

    private final ProviderEsService providerSearchService;

    public ProviderSearchRest(ProviderEsService providerSearchService) {
        this.providerSearchService = providerSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity keywordSearch(String keyword) {
        return response(providerSearchService.searchKeyword(keyword));
    }
}

package com.how.arybyungprovider.service.validation;

import com.how.arybyungcommon.client.danggnmarket.DanggnMarketParser;
import com.how.muchcommon.model.type.ArticleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DanggnValidation implements ValidationMarket {
    private final DanggnMarketParser danggnMarketParser;

    @Override
    public ArticleState validationMarket(long articleId) {
        return danggnMarketParser.validationArticle(articleId) ? ArticleState.S : ArticleState.D;
    }
}

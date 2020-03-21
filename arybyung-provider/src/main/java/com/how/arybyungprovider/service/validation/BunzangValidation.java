package com.how.arybyungprovider.service.validation;

import com.how.arybyungcommon.client.bunjang.BunjangApiClient;
import com.how.arybyungcommon.client.bunjang.model.BunjangItemResponse;
import com.how.muchcommon.model.type.ArticleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class BunzangValidation implements ValidationMarket {

    private final BunjangApiClient bunjangApiClient;

    @Override
    public ArticleState validationMarket(long articleId) {
        try {
            Response<BunjangItemResponse> response = bunjangApiClient.getArticle(articleId).execute();

            if(response.body().getSellerInfo() == null) {
                return ArticleState.D;
            }
        } catch (IOException e) {
            log.error("validation Bunjang {} = > IOException : {}", articleId, e.getMessage());
        }

        return ArticleState.S;
    }
}

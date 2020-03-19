package com.how.arybyungprovider.service.validation;

import com.how.arybyungcommon.client.joonggonara.JoonggonaraApiClient;
import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraArticleDetailResponse;
import com.how.muchcommon.model.type.ArticleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoonggonaraValidation implements ValidationMarket {
    private final JoonggonaraApiClient joonggonaraApiClient;

    @Override
    public ArticleState validationMarket(long articleId) {
        try {
            Response<JoonggonaraArticleDetailResponse> response = joonggonaraApiClient.getArticleDetail(articleId).execute();

            if(response.code() == 200) {
                String status = response.body().getSaleInfo().getSaleStatus();

                if(!status.equals("SALE") && !status.equals("ESCROW"))
                    return ArticleState.C;

            } else if(response.code() == 404) { // 삭제된 게시글일 경우
                return ArticleState.D;
            }
        } catch (IOException e) {
            log.error("validation JoonggoNARA {} = > IOException : {}", articleId, e.getMessage());
        }

        return ArticleState.S;
    }
}

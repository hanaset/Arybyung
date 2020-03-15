package com.how.arybyungobserver.client.joonggonara.model;

import lombok.Data;

@Data
public class JoonggonaraArticleDetailResponse {

    private Long cafeId;

    private Long articleId;

    private JoonggonaraArticleDetail article;

    private JoonggonaraSaleInfo saleInfo;
}

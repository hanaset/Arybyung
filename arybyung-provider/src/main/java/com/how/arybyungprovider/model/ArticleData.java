package com.how.arybyungprovider.model;

import com.how.muchcommon.model.type.ArticleState;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class ArticleData {

    private Long articleId;

    private String subject;

    private String content;

    private Long price;

    private String url;

    private String site;

    private String image;

    private ArticleState state;

    private ZonedDateTime postingDtime;

}

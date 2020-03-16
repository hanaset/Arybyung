package com.how.arybyungprovider.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.how.muchcommon.model.type.ArticleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleData {

    private Long id;

    @JsonProperty("article_id")
    private Long articleId;

    private String subject;

    private String content;

    private Long price;

    private String url;

    private String site;

    private String image;

    private ArticleState state;

    @JsonProperty("posting_dtime")
    private Long postingDtime;

    @JsonProperty("upd_dtime")
    private Long updDtime;

}

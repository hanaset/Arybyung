package com.how.arybyungprovider.model;

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

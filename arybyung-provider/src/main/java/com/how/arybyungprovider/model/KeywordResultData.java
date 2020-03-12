package com.how.arybyungprovider.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeywordResultData {

    @JsonProperty("today_highest_price")
    private Long todayHighestPrice;

    @JsonProperty("today_lowest_price")
    private Long todayLowestPrice;

    @JsonProperty("this_week_highest_price")
    private Long thisWeekHighestPrice;

    @JsonProperty("this_week_lowest_price")
    private Long thisWeekLowestPrice;

    @JsonProperty("article_list")
    private List<ArticleData> articleList;
}

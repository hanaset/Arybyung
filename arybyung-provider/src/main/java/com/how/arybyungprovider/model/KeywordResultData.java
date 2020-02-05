package com.how.arybyungprovider.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class KeywordResultData {

    private Long todayHighestPrice;

    private Long todayLowestPrice;

    private Long thisWeekHighestPrice;

    private Long thisWeekLowestPrice;

    private List<ArticleData> articleList;
}

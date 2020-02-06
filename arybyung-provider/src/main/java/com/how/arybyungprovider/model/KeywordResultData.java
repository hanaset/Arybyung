package com.how.arybyungprovider.model;

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

    private Long todayHighestPrice;

    private Long todayLowestPrice;

    private Long thisWeekHighestPrice;

    private Long thisWeekLowestPrice;

    private List<ArticleData> articleList;
}

package com.how.arybyungcommon.client.bunjang.model;

import lombok.Data;

import java.util.List;

@Data
public class BunjangHomeResponse {

    private List<BunjangHomeItem> list;

    private String result;
}

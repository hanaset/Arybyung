package com.how.arybyungobserver.client.joonggonara.model;

import lombok.Data;

import java.util.List;

@Data
public class JoonggonaraResult {

    private Long cafeId;

    private String cafeName;

    private Boolean cafeStaff;

    private Boolean cafeMember;

    private List<JoonggonaraArticle> articleList;
}

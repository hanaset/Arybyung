package com.how.arybyungobserver.client.joonggonara.model;

import lombok.Data;

@Data
public class JoonggonaraArticle {

    private Long cafeId;

    private Long articleId;

    private Long refArticleId;

    private String replyListOrder;

    private Long menuId;

    private String menuName;

    private String menuType;

    private Boolean restrictMenu;

    private String boardType;

    private String subject;

    private Long cost;

    private String representImage;

    private Long writeDateTimestamp;
}

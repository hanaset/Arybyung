package com.how.arybyungobserver.client.joonggonara.model;

import lombok.Data;

@Data
public class JoonggonaraArticleDetail {

    private Long id;

    private Long refArticleId;

    private String subject;

    private Long writeDate;

    private String content;
}

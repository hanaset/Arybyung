package com.how.arybyungprovider.model.kakao.response.template.listcard;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListCardHeader {
    public String title;
    public String imageUrl;
}

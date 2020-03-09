package com.how.arybyungprovider.model.kakao.response.template.listcard;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ListCardItem {
    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("imageUrl")
    public String imageUrl;

    @SerializedName("link")
    public Map<String, String> link;
}

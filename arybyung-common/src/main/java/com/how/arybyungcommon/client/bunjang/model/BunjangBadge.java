package com.how.arybyungcommon.client.bunjang.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BunjangBadge {

    private String label;

    @SerializedName("badge_type")
    private String badgeType;

    @SerializedName("created_at")
    private Long createdAt;

    @SerializedName("expired_at")
    private Long expiredAt;

    private String desc;

    private String status;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("detail_page_url")
    private String detailPageUrl;
}

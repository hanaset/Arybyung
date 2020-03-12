package com.how.arybyungobserver.client.bunjang.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class BunjangSellerInfo {

    private String uid;

    @SerializedName("user_name")
    private String username;

    private Boolean bizseller;

    @SerializedName("num_item")
    private String numItem;

    @SerializedName("num_grade_avg")
    private String numGradeAvg;

    @SerializedName("num_follower")
    private String numFollower;

    @SerializedName("num_review")
    private String numReview;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("is_withdraw")
    private String isWithdraw;

    @SerializedName("join_date")
    private Long joinDate;

    @SerializedName("access_time")
    private Long accessTime;

    @SerializedName("is_identification")
    private Boolean isIdentification;

    private List<BunjangBadge> badges;

    private String result;
}

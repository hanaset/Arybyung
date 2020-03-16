package com.how.arybyungcommon.client.bunjang.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BunjangHomeItem {

    private Boolean ad;

    private Boolean checkout;

    @SerializedName("contact_hope")
    private Boolean contactHope;

    @SerializedName("free_shipping")
    private Boolean freeShipping;

    @SerializedName("is_adult")
    private Boolean isAdult;

    private String name;

    @SerializedName("num_faved")
    private String numFaved;

    private String pid;

    private String price;

    @SerializedName("product_image")
    private String productImage;

    private String status;

    private String style;

    private String uid;

    private Double update_time;
}

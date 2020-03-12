package com.how.arybyungobserver.client.bunjang.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BunjangItemResponse {

    @SerializedName("item_info")
    private BunjangItemInfo itemInfo;

    @SerializedName("seller_info")
    private BunjangSellerInfo sellerInfo;
}

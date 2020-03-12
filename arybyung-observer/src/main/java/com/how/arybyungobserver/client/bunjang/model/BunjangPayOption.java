package com.how.arybyungobserver.client.bunjang.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BunjangPayOption {

    @SerializedName("pay_type")
    private String payType;

    private Boolean shipping;

    @SerializedName("in_person")
    private Boolean inPerson;
}

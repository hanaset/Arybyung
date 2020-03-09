package com.how.arybyungprovider.model.kakao.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Bot {
    @SerializedName("id")
    @Expose
    public String botId;
    @SerializedName("name")
    @Expose
    public String botName;
}

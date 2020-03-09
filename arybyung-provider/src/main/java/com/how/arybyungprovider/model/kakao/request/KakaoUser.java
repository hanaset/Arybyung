package com.how.arybyungprovider.model.kakao.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class KakaoUser {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("type")
    @Expose
    public String type;
//    @SerializedName("properties")
//    @Expose
//    public Properties properties;
}

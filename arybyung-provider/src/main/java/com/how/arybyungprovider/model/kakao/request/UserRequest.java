package com.how.arybyungprovider.model.kakao.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("timezone")
    @Expose
    public String timezone;
//    @SerializedName("params")
//    @Expose
//    public Params params;
    @SerializedName("block")
    @Expose
    public Block block;
    @SerializedName("utterance")
    @Expose
    public String utterance;
    @SerializedName("lang")
    @Expose
    public Object lang;
    @SerializedName("user")
    @Expose
    public KakaoUser user;
}

package com.how.arybyungprovider.model.kakao.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoRequest {
    @SerializedName("intent")
    @Expose
    public Intent intent;
    @SerializedName("userRequest")
    @Expose
    public UserRequest userRequest;
    @SerializedName("bot")
    @Expose
    public Bot bot;
    @SerializedName("action")
    @Expose
    public Action action;
}

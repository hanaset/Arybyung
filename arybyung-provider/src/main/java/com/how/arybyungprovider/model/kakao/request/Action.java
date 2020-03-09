package com.how.arybyungprovider.model.kakao.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class Action {
    @SerializedName("name")
    @Expose
    public String botName;
//    @SerializedName("clientExtra")
//    @Expose
//    public Object clientExtra;
    @SerializedName("params")
    @Expose
    public Map<String, String> params;
    @SerializedName("id")
    @Expose
    public String actionId;
    @SerializedName("detailParams")
    @Expose
    public DetailParams detailParams;
}

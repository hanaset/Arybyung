package com.how.arybyungprovider.model.kakao.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
public class KakaoResponseTemplate<T> {

    @SerializedName("outputs")
    @Expose
    public List<Map<String, T>> outputs;
}

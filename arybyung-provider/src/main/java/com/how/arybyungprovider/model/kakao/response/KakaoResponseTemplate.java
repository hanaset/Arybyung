package com.how.arybyungprovider.model.kakao.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class KakaoResponseTemplate<T> {
    private T t;

    @SerializedName("outputs")
    @Expose
    public List<T> outputs;
}

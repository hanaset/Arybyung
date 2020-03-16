package com.how.arybyungcommon.client.bunjang.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BunjangItemInfo {

    private String pid;

    private String uid;

    private String name;

    private String status;

    private String price;

    private Boolean ad;

    private String style;

    @SerializedName("group_ids")
    private String groupIds;

//    private List<String> groups;

    @SerializedName("num_faved")
    private String numFaved;

    @SerializedName("num_item_view")
    private String numItemView;

    @SerializedName("num_comment")
    private String numComment;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("update_time")
    private Long updateTime;

    @SerializedName("free_shipping")
    private Boolean freeShipping;

    @SerializedName("is_free_sharing")
    private Boolean isFreeSharing;

    @SerializedName("is_adult")
    private Boolean isAdult;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("product_image")
    private String productImage;

    private Boolean bizseller;

    private Boolean checkout;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("category_name")
    private List<Map<String, String>> categoryName;

    private String description;

    @SerializedName("description_for_detail")
    private String descriptionForDetail;

    @SerializedName("full_description_webview_url")
    private String fullDescriptionWebviewUrl;

    @SerializedName("full_description_webview_type")
    private String fullDescriptionWebviewType;

    @SerializedName("full_description_webview_btn")
    private String fullDescriptionWebviewBtn;

    private Boolean tradable;

    private Boolean used;

    @SerializedName("used_code")
    private Long usedCode;

    @SerializedName("naver_shopping_url")
    private String naverShoppingUrl;

    @SerializedName("image_count")
    private Long imageCount;

    @SerializedName("image_source")
    private String ImageSource;

    private String location;

    private String latitude;

    private String longitude;

    private Long qty;

    private String keyword;

    @SerializedName("contact_enabled")
    private Boolean contactEnabled;

    @SerializedName("comment_enabled")
    private Boolean commentEnabled;

    @SerializedName("ordernow_enabled")
    private Boolean ordernowEnabled;

    @SerializedName("ordernow_label")
    private String ordernowLabel;

    @SerializedName("ordernow_webview")
    private String ordernowWebview;

    @SerializedName("ordernow_token_required")
    private Boolean ordernowTokenRequired;

    @SerializedName("ordernow_url")
    private String ordernowUrl;

    @SerializedName("contact_hope")
    private Boolean contactHope;

    private Boolean bunpay;

    @SerializedName("is_location_confirm")
    private Long isLocationConfirm;

    @SerializedName("is_buncar")
    private Boolean isBuncar;

    @SerializedName("private")
    private Boolean isPrivate;

    @SerializedName("only_neighborhood")
    private Boolean onlyNeighborhood;

    @SerializedName("neighborhood_option")
    private String neighborhoodOption;

    private String specification;

//    @SerializedName("extended_spec")
//    private List<Long> extendedSpec;

    @SerializedName("pay_option")
    private BunjangPayOption payOption;
}

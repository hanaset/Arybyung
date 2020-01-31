package com.how.arybyungprovider.web.controller.support;

public abstract class ProviderControllerSupport {

    protected String redirect(String url) {
        return "redirect:".concat(url);
    }
}

package com.how.arybyungprovider.web.controller;

import com.how.arybyungprovider.web.controller.support.ProviderControllerSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProviderSwaggerController extends ProviderControllerSupport {

    @GetMapping("/swagger")
    public String redirect() {
        return redirect("/swagger-ui.html");
    }
}

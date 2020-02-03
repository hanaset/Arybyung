package com.how.arybyungprovider.web.rest.swagger;

import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProviderSwaggerController extends ProviderRestSupport {

    @GetMapping("/swagger")
    public String redirect() {
        return redirect("/swagger-ui.html");
    }
}

package com.how.arybyungprovider.web.rest.swagger;

import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(tags = "HowMuch Swagger")
public class ProviderSwaggerController extends ProviderRestSupport {

    @GetMapping("/swagger")
    public String redirect() {
        return redirect("/swagger-ui.html");
    }
}

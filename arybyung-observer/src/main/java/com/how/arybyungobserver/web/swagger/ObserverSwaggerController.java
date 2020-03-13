package com.how.arybyungobserver.web.swagger;

import com.how.arybyungobserver.web.rest.support.ObserverRestSupport;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(tags = "HowMuch Swagger")
public class ObserverSwaggerController extends ObserverRestSupport {

    @GetMapping("/swagger")
    public String redirect() {
        return redirect("/swagger-ui.html");
    }
}

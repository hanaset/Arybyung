package com.how.arybyungobserver.web.rest;

import com.how.arybyungobserver.service.ObserverControlService;
import com.how.arybyungobserver.web.rest.support.ObserverRestSupport;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/controller")
@Api(tags = "HowMuch Observer Service API")
public class ObserverControlRest extends ObserverRestSupport {

    private final ObserverControlService observerControlService;

    public ObserverControlRest(ObserverControlService observerControlService) {
        this.observerControlService = observerControlService;
    }

    @PostMapping("/site_on")
    public ResponseEntity siteOn(@RequestBody List<String> siteList) {
        observerControlService.siteOnOff(siteList, "Y");
        return response("ok");
    }

    @PostMapping("/site_off")
    public ResponseEntity siteOff(@RequestBody List<String> siteList) {
        observerControlService.siteOnOff(siteList, "N");
        return response("ok");
    }

    @GetMapping("/site_status")
    public ResponseEntity getSiteStatus() {
        return response(observerControlService.getSiteController());
    }

}

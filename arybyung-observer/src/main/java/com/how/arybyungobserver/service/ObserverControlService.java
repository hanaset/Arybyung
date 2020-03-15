package com.how.arybyungobserver.service;

import com.google.common.collect.Maps;
import com.how.muchcommon.entity.jpaentity.SiteEntity;
import com.how.muchcommon.repository.jparepository.SiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ObserverControlService {

    private final SiteRepository siteRepository;
    private final Map<String, String> siteController;

    public ObserverControlService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
        this.siteController = Maps.newHashMap();
    }

    @PostConstruct
    public void initSiteController() {
        List<SiteEntity> siteEntities = siteRepository.findAll();

        siteEntities.stream().forEach(siteEntity -> {
            siteController.put(siteEntity.getCode(), siteEntity.getIsCheck());
        });
    }

    public Boolean checkSite(String site) {
        return siteController.getOrDefault(site, "N").equals("Y");
    }

    @Transactional
    public void siteOnOff(List<String> siteList, String onoff) {

        List<SiteEntity> siteEntities = siteList.stream().map(site -> {
            SiteEntity entity = siteRepository.findByCode(site);
            entity.setIsCheck(onoff);
            siteController.put(site, onoff);
            return entity;
        }).collect(Collectors.toList());

        siteRepository.saveAll(siteEntities);

        log.info("{} ==> {}", siteList, onoff.equals("Y") ? "ON" : "OFF");
    }

    public Map<String, String> getSiteController() {
        return siteController;
    }
}

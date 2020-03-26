package com.how.arybyungobserver.service;

import com.how.muchcommon.entity.jpaentity.CrawlingStatEntity;
import com.how.muchcommon.entity.jpaentity.id.CrawlingStatId;
import com.how.muchcommon.model.type.MarketName;
import com.how.muchcommon.repository.jparepository.CrawlingStatRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class CrawlingStatService {

    private final CrawlingStatRepository crawlingStatRepository;

    public CrawlingStatService(CrawlingStatRepository crawlingStatRepository) {
        this.crawlingStatRepository = crawlingStatRepository;
    }

    public CrawlingStatEntity init(MarketName marketName) {

        CrawlingStatId id = CrawlingStatId.builder()
                .date(Date.valueOf(LocalDate.now()))
                .site(marketName.getName())
                .build();

        return crawlingStatRepository.findById(id)
                .orElse(CrawlingStatEntity.builder()
                    .id(id)
                    .filteringCount(0L)
                    .successCount(0L)
                    .failCount(0L)
                    .build()
                );
    }

    public void save(MarketName marketName, Long success, Long fail, Long filtering) {

        CrawlingStatId id = CrawlingStatId.builder()
                .date(Date.valueOf(LocalDate.now()))
                .site(marketName.getName())
                .build();

        CrawlingStatEntity entity =  crawlingStatRepository.findById(id)
                .orElse(CrawlingStatEntity.builder().build());

        entity.setId(id);
        entity.setSuccessCount(success);
        entity.setFailCount(fail);
        entity.setFilteringCount(filtering);

        crawlingStatRepository.save(entity);
    }
}

package com.how.arybyungprovider.service;

import com.how.muchcommon.entity.elasticentity.ArticleEsEntity;
import com.how.muchcommon.repository.elasticrepository.ArticleEsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProviderSearchService {

    private final ArticleEsRepository articleEsRepository;

    public ProviderSearchService(ArticleEsRepository articleEsRepository) {
        this.articleEsRepository = articleEsRepository;
    }

    public List<ArticleEsEntity> searchKeyword(String keyword) {

        LocalDateTime startDate = LocalDateTime.now().minusDays(7);

//        List<ArticleEsEntity> articleEsEntities = articleEsRepository.findBySubjectLikeAndPostingDtimeAfter(keyword, startDate.format(dateTimeFormatter));
        List<ArticleEsEntity> articleEsEntities = articleEsRepository.findBySubjectLike(keyword);

        return articleEsEntities;
    }
}

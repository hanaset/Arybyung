package com.hanaset.arybyungcommon.repository;

import com.hanaset.arybyungcommon.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    ArticleEntity findTopByOrderByArticleIdDesc();
    Optional<ArticleEntity> findTopBySiteOrderByArticleIdDesc(String site);
}

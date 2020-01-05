package com.hanaset.arybyungcommon.repository;

import com.hanaset.arybyungcommon.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    
    ArticleEntity findTopByOrderByArticleIdDesc();
}

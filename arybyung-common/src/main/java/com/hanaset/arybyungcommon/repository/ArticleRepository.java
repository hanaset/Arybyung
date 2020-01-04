package com.hanaset.arybyungcommon.repository;

import com.hanaset.arybyungcommon.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    List<ArticleEntity> findByPostingDtimeAfter(ZonedDateTime date);
}

package com.hanaset.arybyungcommon.repository;

import com.hanaset.arybyungcommon.entity.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<SiteEntity, String> {
}

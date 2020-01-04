package com.hanaset.arybyungcommon.entity;

import com.hanaset.arybyungcommon.converter.ZonedDateTimeConverter;
import com.hanaset.arybyungcommon.model.type.ArticleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ARTICLE")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long articleId;

    private String subject;

    private Long price;

    private String url;

    private String site;

    private String image;

    @Enumerated(EnumType.STRING)
    private ArticleState state;

    private String content;

    @Column(name = "posting_dtime")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime postingDtime;

    @Column(name = "reg_dtime")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime regDtime;

    @Column(name = "upd_dtime")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime updDtime;
}

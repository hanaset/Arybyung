package com.how.arybyungprovider;

import com.how.arybyungprovider.service.ProviderArticleService;
import com.how.muchcommon.entity.elasticentity.ArticleEsEntity;
import com.how.muchcommon.repository.elasticrepository.ArticleEsRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@ContextConfiguration
class ValidationArticleTest {

    @Autowired
    private ArticleEsRepository articleEsRepository;

    @Autowired
    private ProviderArticleService providerArticleService;

    @Test
    public void 게시글테스트() {
        ArticleEsEntity esEntity = ArticleEsEntity.builder().id(52874L).build();
        articleEsRepository.delete(esEntity);
    }

//    @Test
//    public void 중고나라_테스트() {
//        providerArticleValidService.validationJoonggonara(707925606L);
//    }
//
//    @Test
//    public void 번개장터_테스트() {
//        providerArticleValidService.validationBunjang(1L);
//    }
//
//    @Test
//    public void 당근마켓_테스트() {
//        providerArticleValidService.validationDanggn(67207753L);
//    }
}

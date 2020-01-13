package com.how.arybyungobserver;

import com.how.arybyungobserver.client.JoonggonaraParser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@ContextConfiguration
class ArybyungObserverApplicationTests {

    @Autowired
    private JoonggonaraParser joonggonaraParser;

    @Test
    void 중고나라_최근글번호_테스트() throws IOException {
        joonggonaraParser.getRecentArticleId();
    }

    @Test
    void 중고나라_최근글데이터_테스트() throws Exception {
//        joonggonaraParser.getArticle("602295788");
    }

    @Test
    void 네이버로그인_테스트() throws Exception {
        joonggonaraParser.naverLogin();
    }

    @Test
    void 네이버로그인_후_파싱_테스트() throws Exception {
        joonggonaraParser.naverLogin();
//        joonggonaraParser.getArticle("602295788");
        joonggonaraParser.getArticle(694781369L);
    }
}
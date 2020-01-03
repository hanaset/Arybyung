package com.hanaset.arybyungobserver;

import com.hanaset.arybyungobserver.component.JoonggonaraParser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    void 중고나라_최근글데이터_테스트() throws IOException {
        joonggonaraParser.getArticle("690308688");
    }

    @Test
    void 네이버로그인_테스트() throws Exception{
        joonggonaraParser.naverLogin();
    }
}

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
    void 중고나라_네이버게시글파싱_테스트() throws IOException {
        joonggonaraParser.postParsing();
    }

}

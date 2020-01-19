package com.how.arybyungobserver.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@ContextConfiguration
class ParsingTest {

    @Autowired
    private FilteringWordService filteringWordService;

    @Test
    void 정규식_테스트() {

        Pattern pattern = filteringWordService.getPattern();

        String test = "아이폰8 삽니다.";
        String test1 = "아이폰";

        Matcher matcher = pattern.matcher(test);
        System.out.println(matcher.find());
        matcher = pattern.matcher(test1);
        System.out.println(matcher.find());
    }
}

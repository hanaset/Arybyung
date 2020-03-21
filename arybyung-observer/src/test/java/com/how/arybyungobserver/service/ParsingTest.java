package com.how.arybyungobserver.service;

import com.how.arybyungcommon.service.FilteringWordService;
import com.how.muchcommon.repository.jparepository.FilterRepository;
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
    private FilterRepository filterRepository;

    @Autowired
    private FilteringWordService filteringWordService;

    @Test
    void 정규식_테스트() {

        Pattern pattern = filteringWordService.getPattern();

        String test = "라이카 summaron 35mm f2.8삽니다(summaron 35mm f2.8)";


        Matcher matcher = pattern.matcher(test);
        System.out.println(matcher.find());
    }
}

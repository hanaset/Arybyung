package com.hanaset.arybyungobserver.component;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JoonggonaraParser {

    public void postParsing() throws IOException {
        Document document = Jsoup.connect("https://cafe.naver.com/ArticleList.nhn?search.clubid=10050146&search.boardtype=L")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
                .header("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .get();

        List<Element> elements = document.select("[class=article]");
        elements.stream().map(element -> {
            Long articleId =  Arrays.asList(element.attr("href").split(";")).stream().filter(s -> s.contains("articleid")).map(s -> s.replaceAll());
        })

        System.out.println(elements);
    }
}

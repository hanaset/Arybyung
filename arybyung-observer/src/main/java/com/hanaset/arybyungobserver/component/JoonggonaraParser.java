package com.hanaset.arybyungobserver.component;

import com.hanaset.arybyungobserver.utils.DriverUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JoonggonaraParser {

    public String getRecentArticleId() throws IOException {
        Document document = Jsoup.connect("https://cafe.naver.com/ArticleList.nhn?search.clubid=10050146&search.boardtype=L")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
                .header("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .get();

        List<Element> elements = document.select("[class=article]");
        String maxArticleId = elements.stream().map(element -> {
            String articleId = Arrays.asList(element.attr("href").split("&")).stream().filter(s -> s.contains("articleid")).map(s -> s.replaceAll("[^0-9]", "")).findFirst().get();

            return articleId;
        }).max(String::compareToIgnoreCase).get();

        System.out.println(maxArticleId);

        return maxArticleId;
    }

    public void getArticle(String articleId) throws IOException {

        String url = "https://cafe.naver.com/ArticleRead.nhn?clubid=10050146&page=1&boardtype=L&articleid=" + articleId + "&referrerAllArticles=true";

        Document document = Jsoup.connect(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
                .header("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .get();

        Element costElement = document.selectFirst("[class=cost]");
        System.out.println(costElement);
        Element imageElement = document.selectFirst("[class=imgs border-sub]");
        System.out.println(imageElement);
        List<Element> elements = document.select("strong");
        System.out.println(elements);

    }

    public Map<String, String> naverLogin() throws Exception {

        System.setProperty("webdriver.gecko.driver", "arybyung-observer/src/main/resources/webDriver/geckodriver");
//        System.setProperty("webdriver.chrome.driver", "./src/main/resources/webDriver/chromedriver"); 테스트코드
        System.setProperty("java.awt.headless", "false");

        WebDriver driver = new FirefoxDriver();
        driver.get("https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com");

        DriverUtil.clipboardCopy("b183523", "//*[@id=\"id\"]", driver);
        DriverUtil.clipboardCopy("thswjdqls56@!", "//*[@id=\"pw\"]", driver);

        driver.findElement(By.xpath("//*[@id=\"frmNIDLogin\"]/fieldset/input")).click();

        Set<Cookie> cookies = driver.manage().getCookies();
        Map<String, String> cookieMaps = cookies.stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));

        return cookieMaps;

    }
}

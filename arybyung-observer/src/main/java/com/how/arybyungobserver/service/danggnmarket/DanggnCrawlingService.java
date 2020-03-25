package com.how.arybyungobserver.service.danggnmarket;

import com.how.arybyungcommon.client.danggnmarket.DanggnMarketClient;
import com.how.arybyungcommon.properties.UrlProperties;
import com.how.arybyungcommon.service.FilteringWordService;
import com.how.arybyungobserver.service.CrawlingStatService;
import com.how.muchcommon.entity.jpaentity.ArticleEntity;
import com.how.muchcommon.entity.jpaentity.CrawlingStatEntity;
import com.how.muchcommon.entity.jpaentity.TopArticleEntity;
import com.how.muchcommon.model.type.ArticleState;
import com.how.muchcommon.model.type.MarketName;
import com.how.muchcommon.repository.jparepository.ArticleRepository;
import com.how.muchcommon.repository.jparepository.TopArticleRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DanggnCrawlingService {

    private Logger log = LoggerFactory.getLogger("danggn_log");

    private final ArticleRepository articleRepository;
    private final CrawlingStatService crawlingStatService;
    private final DanggnMarketClient danggnMarketClient;
    private final FilteringWordService filteringWordService;
    private final TopArticleRepository topArticleRepository;
    private final UrlProperties urlProperties;

    private AtomicLong successCount = new AtomicLong(0);
    private AtomicLong failCount = new AtomicLong(0);
    private AtomicLong filteringCount = new AtomicLong(0);

    public DanggnCrawlingService(ArticleRepository articleRepository,
                              FilteringWordService filteringWordService,
                              DanggnMarketClient danggnMarketClient,
                              CrawlingStatService crawlingStatService,
                              TopArticleRepository topArticleRepository,
                              UrlProperties urlProperties) {
        this.articleRepository = articleRepository;
        this.crawlingStatService = crawlingStatService;
        this.filteringWordService = filteringWordService;
        this.danggnMarketClient = danggnMarketClient;
        this.topArticleRepository = topArticleRepository;
        this.urlProperties = urlProperties;
    }

    public void saveCount() {
        crawlingStatService.save(MarketName.bunjang, successCount.get(), failCount.get(), filteringCount.get());
        init();
    }

    @PostConstruct
    public void init() {
        CrawlingStatEntity entity = crawlingStatService.init(MarketName.bunjang);
        successCount.set(entity.getSuccessCount());
        failCount.set(entity.getFailCount());
        filteringCount.set(entity.getFilteringCount());
    }

    public TopArticleEntity getTopArticleEntity() {
        return topArticleRepository.findBySite(MarketName.danggn.getName()).orElse(TopArticleEntity.builder().articleId(0L).build());
    }

    public void setTopArticleEntity(TopArticleEntity topArticleEntity) {
        topArticleRepository.save(topArticleEntity);
    }

    public Long getRecentArticleId() throws IOException {
        Document document = danggnMarketClient.getRecentArticle();

        List<Element> elements = document.select("[data-event-label]");
        Long maxArticleId = elements.stream().map(element -> Long.parseLong(element.attr("data-event-label"))).max(Long::compareTo).orElse(0L);

        return maxArticleId;
    }

    @Async("danggnMarketTaskExecutor")
    public void separationGetArticles(Long start, Long unit) {
        for (Long articleId = start; articleId < start + unit; articleId++) {
            getArticle(articleId);
        }
    }

    public void getArticle(Long articleId) {

        String url = urlProperties.getDanggnArticleUrl() + "/articles/" + articleId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {

            Document document = danggnMarketClient.getArticle(articleId);

            Element subjectElement = document.getElementById("article-title");

            Element priceElement = document.getElementById("article-price") == null ? document.getElementById("article-price-nanum") : document.getElementById("article-price");
            String stringPrice = priceElement.text().replaceAll("[^0-9]", "");

            ArticleState state = priceElement.text().equals("무료나눔") ? ArticleState.F : ArticleState.S;
            Long price = Long.parseLong(stringPrice.equals("") ? "0" : stringPrice);

            Element imageElement = document.selectFirst("[class=image-wrap]").child(0);
            Element regionElement = document.getElementById("region-name");
            Element dateElement = document.selectFirst("[datatype=xsd:date]");
            Element contentElement = document.getElementById("article-detail");

            String content = contentElement.text() + "\n" + regionElement.text();

            if (filteringWordService.stringFilter(subjectElement.text()) || filteringWordService.stringFilter(content)) {
                log.info("danggn filtering Check Failed : {}", articleId);
                filteringCount.incrementAndGet();
                return;
            }

            ArticleEntity articleEntity = ArticleEntity.builder()
                    .articleId(articleId)
                    .url(url)
                    .subject(subjectElement.text())
                    .price(price)
                    .state(state)
                    .image(imageElement.attr("data-lazy"))
                    .site(MarketName.danggn.getName())
                    .postingDtime(LocalDate.parse(dateElement.attr("content"), formatter).atStartOfDay(ZoneId.of("Asia/Seoul")).minusYears(2))
                    .content(content)
                    .build();

            articleRepository.save(articleEntity);
            successCount.incrementAndGet();
        } catch (NullPointerException | IOException e) {
            failCount.incrementAndGet();
            log.error("danggn Not found Article : {}", articleId);
        }
    }
}

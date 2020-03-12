package com.how.arybyungobserver;

import com.how.arybyungobserver.client.joonggonara.JoonggonaraParser;
import com.how.arybyungobserver.client.DriverConstants;
import com.how.arybyungobserver.service.bunjang.BunjangService;
import com.how.arybyungobserver.service.FilteringWordService;
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

    @Autowired
    private BunjangService bunjangService;

    @Autowired
    private FilteringWordService filteringWordService;

    public void beFore() {
        System.setProperty("webdriver.gecko.driver", DriverConstants.TEST_DRIVER_PATH); //테스트코드
        System.setProperty("java.awt.headless", "false");
    }

    @Test
    void 번개장터_테스트() {
        beFore();
        bunjangService.parsingArticle();
    }
    @Test
    void 중고나라_최근글번호_테스트() throws IOException {
        joonggonaraParser.getRecentArticleId();
    }

    @Test
    void 네이버로그인_테스트() throws Exception {
        joonggonaraParser.naverLogin();
    }

    @Test
    void 네이버로그인_후_파싱_테스트() throws Exception {
//        joonggonaraParser.getArticle(694878920L);
//        joonggonaraParser.getArticle(694781369L);
        joonggonaraParser.getArticle(694890065L);
    }

    @Test
    void 글자_갯수_테스트() {
        String text = " 2기가 3000원에팝니다. 3,000원 판매자정보 treasurer90@naver.com 거래방법 직접거래 배송방법 판매자와 직접 연락하세요 직접거래 시 아래 사항에 유의해주세요. 불확실한 판매자(본인 미인증, 해외IP, 사기의심 전화번호)의 물건은 구매하지 말아주세요. 판매자와의 연락은 메신저보다는 전화, 메일 등을 이용하시고 개인정보 유출에 주의하세요. 계좌이체 시 선입금을 유도할 경우 안전한 거래인지 다시 한 번 확인해주세요. 네이버에 등록된 판매 물품과 내용은 개별 판매자가 등록한 것으로서, 네이버카페는 등록을 위한 시스템만 제공하며 내용에 대하여 일체의 책임을 지지 않습니다. * 거래전 필독! 주의하세요! * 연락처가 없이 외부링크, 카카오톡, 댓글로만 거래할 때 * 연락처 및 계좌번호를 사이버캅과 더치트로 꼭 조회해보기 * 업체인 척 위장하여 신분증과 사업자등록증을 보내는 경우 * 고가의 물품(휴대폰,전자기기)등만 판매하고 최근(1주일 내) 게시글만 있을 때 * 해외직구로 면세받은 물품을 판매하는 행위는 불법입니다. SKT데이터 2기가 3천원에 팝니다. 0.1.0 / 9.4.7.7 / 5.3.4.8 연락주세요";
        System.out.println(text.length());
    }
}
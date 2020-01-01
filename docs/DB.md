# DB 구조

<br>

### 중고 매물 테이블 **(TB_ITEM)**
```
id : 시퀀스
subject : 게시글 제목
price : 제품 가격
url : 게시글 주소
site : 사이트 코드 (TC_SITE)
image : 게시글 이미지 (가능하다면)
posting_dtime : 게시글 작성날짜
reg_dtime : 등록날짜
upd_dtime : 수정날짜
```

<br>

### 사이트 코드 테이블 **(TC_SITE)**
```
code : 해당 사이트에 대한 코드
name : 사이트 명
url : 사이트 url
reg_dtime : 등록날짜
upd_dtime : 수정날짜
```

<br>

### 키워드 테이블 **(TC_KEYWORD)**
```
keyword : 키워드
count : 중복 카운트
reg_dtime : 등록날짜
upd_dtime : 수정날짜
```

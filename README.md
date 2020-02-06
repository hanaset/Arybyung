# Arybyung
중고제품들에 대한 시세와 오더북을 제공해주는 웹사이트🖥

중고나라, 당근마켓, 번개장터에 대한 매물을 크롤링하여 데이터를 수집


### 개발 환경
Elastic Search 6.8.6<br>
Logstash<br>
Redis<br>
Mysql

### 실행 방법
```
Logstash install
Elastic Search install

$brew install logstash
$brew install elasticsearch

$elasticsearch -d
$logstash -f ./logstash/logstash.conf
```



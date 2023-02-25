# 데이터 삽입
# ST_GeomFromText('POINT(경도 위도)')
INSERT INTO `search_by_coordinates` (`xy`) 

VALUES (ST_GeomFromText('POINT(128.88940080059663 35.228550792631175)'));


# 데이터 조회
#  ST_AsText : Binary 데이터로 저장된 POINT 타입을 문자열로 바꿔준다
SELECT 
	`pno` '번호', 
	`name` '장소', 
	`LONGTITUDE` AS '경도', 
	`LATITUDE`  '위도', 
	ST_AsText (`xy`) AS '좌표' 
FROM `search_by_coordinates`;

# 반경으로 검색하기
# WHERE문의 숫자는 기준 좌표로부터의 반경값을 입력(m단위)
SELECT * 
FROM `search_by_coordinates`
WHERE ST_Distance_Sphere(`xy`, ST_GeomFromText('POINT(129.059529 35.157875)')) <= 17000;


# 모서리 좌표 값을 이용한 검색하기
# ST_GeomFromText('LineString(남서좌표, 북동좌표)')
SELECT 
	`pno` '번호', 
	`name` '장소', 
	`LONGTITUDE` AS '경도', 
	`LATITUDE`  '위도', 
	ST_AsText (`xy`) AS '좌표' 
from `search_by_coordinates` 
WHERE MBRContains(ST_GeomFromText('LineString(129.03060372697024 35.13580474054441, 129.11081371521917 35.16456822146159)'), `xy`)
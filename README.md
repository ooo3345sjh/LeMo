# LEMO
<img src="https://user-images.githubusercontent.com/111489860/235365625-ed717f1e-3bce-4e95-a261-7b1b296af02f.png" width="auto" height="70">

블로그형 숙박 예약 웹사이트<br/>
Blog type Hotel Booking Site<br/>
<br/>

## 프로젝트 산출물
[LEMO Project 스프레드시트](https://docs.google.com/spreadsheets/d/1g1_pXSo88nbbJbCdhzgQ4fqB4OPCF-Uaa3gWi8o0Ogw/edit#gid=0) - 개발 기간 동안의 회의록, 일정표, 플로우 차트 등에 대한 자료들이 정리되어 있습니다.<br/>
[LEMO Project 시연 동영상](https://www.youtube.com/watch?v=9E9F-54QE7I&list=PLb318OBaoPW6TAWEBuqGlLEq1Rnh49xxb) - 개발한 서비스를 시연하는 영상입니다.<br/>
[LEMO Project 발표 보고서](https://drive.google.com/open?id=1OY-IOgBKALJBLt5tZiWnUAR-s5fmApfk) - 개발한 서비스 발표 보고서입니다.

## 프로젝트 소개 Introduction
프로젝트에 대한 간략한 소개와 의의를 설명합니다.

### 프로젝트 이름의 의미 Meaning of LeMo
레모(LeMo)는 "Lettuce Memories"의 약자로, 상쾌하고 가볍게 호텔 예약을 할 수 있다는 이미지를 담고 있습니다.</br>
레몬과 비슷한 발음이어서 상쾌한 느낌도 함께 전달하고자 지은 이름입니다.

### 프로젝트의 목적 Why we started this project
여행 계획을 세우는 것은 항상 즐거운 일이지만, 여행 장소에 대한 정보를 수집하고 검증하는 것은 그만큼 귀찮고 어려운 일입니다. 대부분의 여행자들은 블로그나 카페 등의 후기를 검색해보게 되는데, 이러한 후기들 중에는 광고성 후기가 많이 포함되어 있어서 실제 이용 후기를 찾기란 쉽지 않은 경우가 많습니다. 그래서 저희는 실제 이용 후기들을 확인할 수 있으며, 바로 예약까지 가능한 숙박 예약사이트를 개발해보면 어떨까라는 것을 시작으로 프로젝트를 진행하게 되었습니다.

## 프로젝트 기술 소개 Technology List
프로젝트에 사용된 언어와 기술, ERD 등을 소개합니다.

### 사용기술 Skills Utilized
<img src="https://user-images.githubusercontent.com/111489860/235429742-04497c3b-8a4b-4005-9a29-4df8f167fc8f.png"  width="700" height="370">

### 특징적 기술 Key Features 
#### API
- [Google OAuth 2.0](https://developers.google.com/identity/protocols/oauth2) - 구글 소셜 로그인
- [Naver OAuth 2.0](https://developers.naver.com/docs/login/api/api.md) - 네이버 소셜 로그인
- [Kakao OAuth 2.0](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api) - 카카오 소셜 로그인
- [Google Places API](https://developers.google.com/maps/documentation/places/web-service/overview) - 여행하고자하는 지역 검색시 자동으로 주소를 완성해주는 API
- [iamport.payment API](https://api.iamport.kr/) - 결제 API를 테스트할 수 있는 API
- [Google Analytics Data API](https://developers.google.com/analytics/devguides/reporting/data/v1?hl=en) - 구글 애널리틱스 보고서 데이터를 조회할 수 있는 API
- [Kakao Map API](https://apis.map.kakao.com/web/documentation/) - 호텔 및 여행 위치를 지도에 표시해주는 API
- [Data.go.kr Open API](https://www.data.go.kr/data/15081808/openapi.do) - 국세청_사업자등록정보 진위확인 및 상태조회 API
- [Naver SENS API](https://api.ncloud-docs.com/docs/ai-application-service-sens-smsv2) - 휴대폰 인증문자 발송 API
- [Google charts](https://developers.google.com/chart?hl=ko) - 통계 데이터 출력을 위한 구글 차트 API
- [Daum postcode API](https://postcode.map.daum.net/guide) - 우편 번호 검색을 위한 다음 우편 번호 API

#### Library 
- [Dropzone](https://www.dropzone.dev/) - 이미지 업로드를 위한 dropzone 라이브러리 
- [FullCalendar](https://fullcalendar.io/docs) - 통계 관리를 위한 Full Calendar 라이브러리 
- [Summernote](https://summernote.org/) - 게시글 작성을 위한 Summernote 라이브러리
- [bxSlider](https://bxslider.com/) - 이미지 슬라이더 
- [slickSlider](https://kenwheeler.github.io/slick/) - 이미지 슬라이더
- [swiperSlider](https://swiperjs.com/) - 이미지 슬라이더


### 프로젝트 규모 Project Scale Analysis Result
<img src="https://user-images.githubusercontent.com/111489860/235433600-4386bc58-11b5-45fe-8930-02e2efc4ec51.PNG"  width="700" height="370">

### ERD
<img src="https://user-images.githubusercontent.com/111489860/236473046-dd5a1b41-2ac0-4304-8263-93f9f0fb35ba.png"  width="700" height="370">
이 프로젝트의 DB는 29개의 테이블으로 이루어져 있습니다.(토큰기반 자동로그인 테이블 포함)

### 정보구조도 Information Architecture
<img src="https://user-images.githubusercontent.com/111489860/235429656-7fa358d6-661e-4ece-99b8-62128ca91b40.PNG"  width="700" height="370">

### 팀원 Contributors
해당 프로젝트에 대한 이슈를 생성하고 싶거나 우리 팀원들에게 연락하고 싶다면 아래로 연락해주세요.

If you have a question or any comment, feel free to open an issue or to DM me on us.

* [서정현](https://github.com/ooo3345sjh)
* [박종협](https://github.com/lazca2080)
* [이원정](https://github.com/Yiwonjeong)
* [이해빈](https://github.com/094haley)
* [황원진](https://github.com/hwangwonjin)

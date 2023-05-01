package kr.co.Lemo.service;

import com.google.analytics.data.v1beta.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import kr.co.Lemo.domain.AnalyticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HelloAnalyticsReportingService {

  @Value("${spring.servlet.multipart.location}")
  private String uploadPath;

  @Value("${analytics.propertyId}")
  private String propertyId;

  private Integer getData(String startDate, String endDate) throws Exception {
    int totalUsers = 0;

    // credentialsJsonPath: 인증 정보가 포함된 Service Account Key 파일의 경로
    String credentialsJsonPath = uploadPath + "/credentials.json";

    // GoogleCredentials: GCP(Google Cloud Platform)에서 제공하는 서비스에 인증된 사용자의 자격 증명을 가져오기 위함
    // fromStream() 메서드: FileInputStream 객체를 인수로 받아 인증 정보를 생성 (Service Account Key 파일을 사용하여 인증 정보 생성)
    GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsJsonPath));

    // BetaAnalyticsDataSettings: Google Analytics Data API를 사용하기 위한 설정 정보를 생성
    BetaAnalyticsDataSettings betaAnalyticsDataSettings = BetaAnalyticsDataSettings.newBuilder()
                                                          .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                                                          .build();

    try (BetaAnalyticsDataClient analyticsData =
                 BetaAnalyticsDataClient.create(betaAnalyticsDataSettings)) {
      // [START analyticsdata_json_credentials_run_report]
      // RunReportRequest : Google Analytics Data API를 사용하여 보고서를 실행하기 위한 요청 객체를 생성
      // 해당 클래스를 사용하여 보고서의 속성, 차원(Dimension), 측정 항목(Metric), 날짜 범위(DateRange) 등을 지정
      // Google Analytics Data API를 사용하여 "city" 차원과 "activeUsers" 측정 항목을 사용하여 2023년 4월 28일부터 4월 29일까지의 데이터를 요청하는 보고서 실행 요청 객체를 만드는 데 사용.
      // dimension, metric 확인 : https://developers.google.com/analytics/devguides/reporting/data/v1/api-schema?hl=ko
      RunReportRequest request =
              RunReportRequest.newBuilder()
                      .setProperty("properties/" + propertyId)
                      .addDimensions(Dimension.newBuilder().setName("date"))      // addDimensions() 메서드는 보고서의 차원(Dimension)을 추가 (ex. 'city' 차원 추가)
                      .addMetrics(Metric.newBuilder().setName("activeUsers"))     // addMetrics() 메서드는 보고서의 측정 항목(Metric, 수집된 데이터에서 계산한 측정값)을 추가, (ex. activeUsers)
                      .addDateRanges(DateRange.newBuilder()
                              .setStartDate(startDate)
                              .setEndDate(endDate)
                      )  // addDateRanges() 메서드는 보고서에 포함할 날짜 범위(DateRange)를 추가
                      .build();

      RunReportResponse response = analyticsData.runReport(request);
      log.debug(response + "");
      for (Row row : response.getRowsList())
        totalUsers += Integer.parseInt(row.getMetricValues(0).getValue());

    }
    return totalUsers;
  }

  public Integer getTodayActiveUser() throws Exception {
    return getData("today", "today");
  }

  public Integer getActiveUser(String startDate, String endDate) throws Exception {
    return getData(startDate, endDate);
  }

}





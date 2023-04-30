package kr.co.Lemo.service;

import com.google.analytics.data.v1alpha.Funnel;
import com.google.analytics.data.v1alpha.RunFunnelReportRequest;
import com.google.analytics.data.v1beta.*;
import com.google.analytics.data.v1beta.Dimension;
import com.google.analytics.data.v1beta.Metric;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;


public class HelloAnalyticsReportingService {
  @Value("${spring.servlet.multipart.location}")
  private String uploadPath;

  public static void main(String... args) throws Exception {
    /**
     * TODO(developer): Replace this variable with your Google Analytics 4 property ID before
     * running the sample.
     */
    String propertyId = "373040723";


    /**
     * TODO(developer): Replace this variable with a valid path to the credentials.json file for
     * your service account downloaded from the Cloud Console.
     */
    String credentialsJsonPath = "C:/Users/ooo33.DESKTOP-56U45AS/Desktop/workspace/LeMo/Lemo/file/credentials_y.json";
    File file = new File(credentialsJsonPath);
    System.out.println(file.exists());
    sampleRunReport(propertyId, credentialsJsonPath);
  }

  // This is an example snippet that calls the Google Analytics Data API and runs a simple report
  // on the provided GA4 property id.
  static void sampleRunReport(String propertyId, String credentialsJsonPath) throws Exception {
    // [START analyticsdata_json_credentials_initialize]
    // Explicitly use service account credentials by specifying
    // the private key file.
    GoogleCredentials credentials =
            GoogleCredentials.fromStream(new FileInputStream(credentialsJsonPath));

    BetaAnalyticsDataSettings betaAnalyticsDataSettings =
            BetaAnalyticsDataSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

    try (BetaAnalyticsDataClient analyticsData =
                 BetaAnalyticsDataClient.create(betaAnalyticsDataSettings)) {
      // [END analyticsdata_json_credentials_initialize]
      // [START analyticsdata_json_credentials_run_report]
//      RunRealtimeReportRequest request =
//              RunRealtimeReportRequest.newBuilder()
//                      .setProperty("properties/" + propertyId)
//                      .addDimensions(Dimension.newBuilder().setName("country"))
//                      .addMetrics(Metric.newBuilder().setName("activeUsers"))
//
//                      .build();

      
      RunReportRequest request =
              RunReportRequest.newBuilder()
                      .setProperty("properties/" + propertyId)
                      .addDimensions(Dimension.newBuilder().setName("city"))
                      .addMetrics(Metric.newBuilder().setName("activeUsers"))
                      .addDateRanges(DateRange.newBuilder().setStartDate("2023-04-28").setEndDate("2023-04-29"))
                      .build();

      // Make the request.
      RunReportResponse response = analyticsData.runReport(request);
//      RunRealtimeReportResponse response = analyticsData.runRealtimeReport(request);
      // [END analyticsdata_json_credentials_run_report]

      // [START analyticsdata_json_credentials_print_report]
      System.out.println("Report result:");
      System.out.println(response.getRowCount());
      System.out.println(response);
      // Iterate through every row of the API response.
      int totalUsers = 0;
      for (Row row : response.getRowsList()) {
        System.out.printf("%s, %s%n", row.getDimensionValues(0).getValue(), row.getMetricValues(0).getValue());

        totalUsers += Integer.parseInt(row.getMetricValues(0).getValue());
      }
      System.out.println("totalUsers = " + totalUsers);
      // [END analyticsdata_json_credentials_print_report]
    }
  }
    
}


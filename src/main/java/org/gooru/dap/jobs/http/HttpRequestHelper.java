
package org.gooru.dap.jobs.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gooru.dap.constants.HttpConstants;
import org.gooru.dap.jobs.http.response.ClassPerformanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author szgooru Created On 04-Apr-2019
 */
public class HttpRequestHelper {

  private final static Logger LOGGER = LoggerFactory.getLogger(HttpRequestHelper.class);
  
  public ClassPerformanceResponse fetchClassPerformances(String uri, String requestData) {
    ClassPerformanceResponse response = null;

    try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
      final HttpPost postRequest = new HttpPost(uri);

      // Set Request headers
      postRequest.addHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);

      // Set request body
      final StringEntity requestPayload = new StringEntity(requestData.toString());
      postRequest.setEntity(requestPayload);
      
      // Execute request
      try (CloseableHttpResponse httpResponse = httpClient.execute(postRequest)) {
        final int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpEntity responseEntity = httpResponse.getEntity();

        String responseBody =
            responseEntity != null ? readResponseBody(responseEntity.getContent()) : null;
        if (statusCode == HttpConstants.HttpStatus.SUCCESS.getCode()) {
          if (responseBody != null && !responseBody.isEmpty()) {
            response = new ObjectMapper().readValue(responseBody, ClassPerformanceResponse.class);
          } else {
            LOGGER.debug("empty response returned from upstream API call");
          }
        } else {
          LOGGER.warn("Upstream API call returned status code: {}", statusCode);
        }
      }
    } catch (Throwable t) {
      LOGGER.error("error while executing http put", t);
    }

    return response;
  }

  private static String readResponseBody(InputStream inputStream) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      if (br.ready()) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
          sb.append(line);
        }
        return sb.toString();
      } else {
        return null;
      }
    }
  }
}

/* Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo.service;

import demo.model.Ad;
import demo.model.Product;
import demo.utils.HttpUtils;
import demo.utils.SpanUtils;
import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.Status;
import io.opencensus.trace.Tracer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class AdsService {
  @Autowired private Tracer tracer;

  @Value("${ads-service-endpoint}")
  private String svcEndpoint;

  /**
   * Get recommended ads from a downstream Ads service. Ads service is called over HTTP. Trace span
   * is also instrumented to capture distributed span across service boundaries.
   *
   * @param product user selected product
   */
  public Ad getRecommendedAd(Product product) {
    // [START get_recommended_ad]
    Span span = SpanUtils.buildSpan(tracer, "Ad Service").startSpan();
    String result;
    String url = "http://" + svcEndpoint + "/servead";
    try (Scope ws = tracer.withSpan(span)) {
      result =
          HttpUtils.callEndpoint(
              url,
              HttpMethod.POST,
              tracer,
              new JSONObject().put("category", product.getCategories().get(0)));
    }
    // [END get_recommended_ad]
    catch (Exception e) {
      span.setStatus(Status.ABORTED);
      span.addAnnotation("Error while calling service");
      result = "";
    }
    span.end();
    return new Ad.AdBuilder()
        .setRedirectUrl(result.split(":")[0])
        .setText(result.split(":")[1])
        .build();
  }
}

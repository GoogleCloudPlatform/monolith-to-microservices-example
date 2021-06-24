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

import demo.config.recommendation.GrpcClientInterceptor;
import demo.microservices.sync.grpc.ListRecommendationsResponse;
import demo.microservices.sync.grpc.RecommendationServiceGrpc;
import demo.microservices.sync.grpc.RecommendationsRequest;
import demo.model.Product;
import demo.utils.SpanUtils;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {
  @Autowired private ProductService productService;
  @Autowired private GrpcClientInterceptor interceptor;
  @Autowired private Tracer tracer;

  @Value("${recommendation-service-endpoint}")
  private String svcEndpoint;

  @Value("${recommendation-service-port}")
  private String svcPort;

  private RecommendationServiceGrpc.RecommendationServiceBlockingStub blockingStub;
  private List<Product> recommendedProducts;

  @PostConstruct
  private void init() throws Exception {
    ManagedChannel managedChannel =
        ManagedChannelBuilder.forAddress(svcEndpoint, Integer.parseInt(svcPort))
            .usePlaintext()
            .build();
    Channel channel = ClientInterceptors.intercept(managedChannel, interceptor);
    blockingStub = RecommendationServiceGrpc.newBlockingStub(channel);
  }

  /**
   * Get recommended products from a downstream Recommendation service. The recommendation service
   * is called using gRPC. Trace span is also instrumented to capture distributed span across
   * service boundaries.
   *
   * @param product user selected product
   */
  public List<Product> getRecommendedProducts(Product product) {
    // [START get_recommended_products]
    Span span = SpanUtils.buildSpan(tracer, "Recommendation Service").startSpan();
    try (Scope ws = tracer.withSpan(span)) {
      recommendedProducts = new ArrayList<Product>();
      ListRecommendationsResponse response =
          blockingStub.listRecommendations(
              RecommendationsRequest.newBuilder()
                  .addAllProductCategory(product.getCategories())
                  .build());
      // [END get_recommended_products]
      for (String pid : response.getProductIdsList()) {
        recommendedProducts.add(productService.findProductById(pid).get());
      }
      recommendedProducts.remove(product);
    }
    span.end();
    return recommendedProducts;
  }
}

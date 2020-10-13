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

package demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import demo.model.Product;
import io.opencensus.contrib.grpc.metrics.RpcViews;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@EnableTransactionManagement
@SpringBootApplication
public class Application {

  private List<Product> productList;
  private Tracer tracer = Tracing.getTracer();

  @Bean
  public Tracer getTracer() {
    return tracer;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  public void init() throws FileNotFoundException, IOException {
    InputStream stream = getClass().getClassLoader().getResourceAsStream("products.json");
    Reader reader = new InputStreamReader(stream, "UTF-8");
    setProductList(new Gson().fromJson(reader, new TypeToken<List<Product>>() {}.getType()));

    // [START register_SD_exporter]
    RpcViews.registerAllGrpcViews();
    StackdriverTraceExporter.createAndRegister(StackdriverTraceConfiguration.builder().build());
    // [END register_SD_exporter]
  }

  @Bean
  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }
}

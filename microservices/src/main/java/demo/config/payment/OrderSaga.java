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

package demo.config.payment;

import io.zeebe.client.ZeebeClient;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Instantiates the zeebe client with the distributed transaction workflow (defined in
 * order-process.bpmn)
 */
@Component
public class OrderSaga {

  @Value("${zeebe-endpoint}")
  private String broker;

  private ZeebeClient zeebe;

  // Initiate workflow. Zeebe is configured to generate a "place-order" event when triggered.
  @PostConstruct
  public void init() throws FileNotFoundException, IOException {
    zeebe = ZeebeClient.newClientBuilder().brokerContactPoint(broker).usePlaintext().build();
    zeebe.newDeployCommand().addResourceFromClasspath("order-process.bpmn").send().join();
  }

  @Bean
  public ZeebeClient zeebe() {
    return zeebe;
  }

  public void setZeebeClient(ZeebeClient zeebe) {
    this.zeebe = zeebe;
  }
}

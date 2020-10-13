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

import demo.model.Cart;
import demo.model.OrderStatus;
import demo.service.OrderService;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import java.time.Duration;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Implements cancel order as part of the checkout workflow in case payment fails
@Component
public class CancelOrder implements JobHandler {

  @Autowired private ZeebeClient zeebe;
  @Autowired private OrderService orderService;
  private JobWorker subscription;

  /**
   * Listen for a "cancel-order" event and execute the handler method. "cancel-order" is generated
   * in case:
   * 1. When the order is first placed, any local failures before downstream services are called.
   * 2. Downstream services (Payment in this example) results in a failure.
   */
  @PostConstruct
  public void subscribe() {
    subscription =
        zeebe
            .newWorker()
            .jobType("cancel-order")
            .handler(this)
            .timeout(Duration.ofMinutes(1))
            .open();
  }

  @PreDestroy
  public void closeSubscription() {
    subscription.close();
  }

  /**
   * Cancel the locally stored cart items in the database.
   *
   * @param client zeebe client
   * @param job zeebe workflow
   */
  @Override
  public void handle(JobClient client, ActivatedJob job) {
    System.out.println("Inside cancel order.");

    // Get cart record from db
    String orderId = new JSONObject(job.getVariables()).getString("orderId");
    System.out.println("Order Id to be canceled: " + orderId);
    Cart cart = orderService.findOrderById(orderId);

    // Update status from pending to failed
    cart.setOrderStatus(Enum.valueOf(OrderStatus.class, "Failed"));
    orderService.addCustomerOrder(cart);
    System.out.println("Updated order details in db");

    // Finish workflow
    client.newCompleteCommand(job.getKey()).send().join();
  }
}

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

package demo.controller;

import demo.model.Cart;
import demo.service.CartService;
import io.zeebe.client.ZeebeClient;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CheckoutController {

  @Autowired private HttpSession session;
  @Autowired private CartService cartService;
  @Autowired private ZeebeClient zeebe;

  // Checkout defined as a workflow orchestrated via <a href="#{@Zeebe}">{@https://zeebe.io/}</a>.
  @RequestMapping(value = "/cart/checkout", method = RequestMethod.POST)
  public String checkout(Model model) throws Exception {
    Cart cart = cartService.getCart();

    // Start BPMN workflow
    zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId("order-process")
        .latestVersion()
        .variables(cart)
        .send()
        .join();

    // Reset cart and render confirmation page to the end user
    cart = cartService.emptyCart();
    session.setAttribute("cart", cart);
    session.setAttribute("cart_size", cart.getCartItems().size());

    return "orderconfirmation";
  }
}

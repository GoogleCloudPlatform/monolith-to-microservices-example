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

import demo.model.Cart;
import demo.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  @Autowired CartRepository cartRepository;

  /**
   * Save customer order in database
   *
   * @param cart items
   * @return saved cart id
   */
  public String addCustomerOrder(Cart cart) {
    cartRepository.save(cart);
    return cart.getId();
  }

  public Cart findOrderById(String orderId) {
    return cartRepository.findOrderById(orderId);
  }
}

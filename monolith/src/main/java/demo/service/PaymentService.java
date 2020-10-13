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

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  /**
   * Dummy payment method which always succeeds. This method is evolved further to mimic failures in
   * the microservices setup when payment service is isolated from the monolith.
   *
   * @return true
   */
  public Boolean makePayment() throws Exception {
    return true;
  }
}

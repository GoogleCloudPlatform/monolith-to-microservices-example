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

import demo.model.Product;
import demo.service.CartService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/** Controls the landing page * */
@Controller
@SessionAttributes(
    "cart_size") // Used by header.ftl that's common across all pages, hence cart_size used as a
// session attribute
public class HomeController {

  @Autowired private List<Product> productList;
  @Autowired private CartService cartService;

  @GetMapping(value = "/")
  public String home(Model model) {
    model.addAttribute("products", productList);
    model.addAttribute("cart_size", cartService.getCart().getCartItems().size());
    return "productlist";
  }
}

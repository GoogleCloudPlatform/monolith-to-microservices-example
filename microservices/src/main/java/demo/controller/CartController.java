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
import demo.model.CartForm;
import demo.model.CartItem;
import demo.service.CartService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CartController {

  @Autowired private HttpSession session;
  @Autowired private CartService cartService;

  /**
   * Add a selected product to the cart. Cart is saved in the session and persisted in database only
   * at the time of checkout.
   *
   * @param formData
   * @param model
   */
  @RequestMapping(value = "/cart", method = RequestMethod.POST)
  public String addToCart(@ModelAttribute("cartForm") CartForm formData, Model model) {

    Cart cart = cartService.getCart();
    List<CartItem> cartItems = cart.getCartItems();

    String selectedProdId = formData.getProduct_id();
    int selectedQty = Integer.parseInt(formData.getQuantity());

    Optional<CartItem> item = cartService.exists(selectedProdId, cartItems);
    if (item.isPresent()) {
      selectedQty = item.get().getQuantity() + selectedQty;
      cartItems.remove(item.get());
    }

    cartItems.add(cartService.newCartItem(selectedProdId, selectedQty, cart));
    cart.setCartItems(cartItems);

    session.setAttribute("cart", cart);
    session.setAttribute("cart_size", cart.getCartItems().size());

    model.addAttribute("cart", cartService.getSelectedProducts(session, cart));
    return "cart";
  }

  /**
   * Show cart items Certain values such as quantity etc. are not included in this implementation
   *
   * @param model
   */
  @GetMapping(value = {"/showcart", "/cart"})
  public String showCart(Model model) {

    Cart cart = cartService.getCart();
    model.addAttribute("cart", cartService.getSelectedProducts(session, cart));
    return "cart";
  }

  /**
   * Empty cart and reset values saved in the session scope
   *
   * @param formData user selected products
   * @param model
   */
  @RequestMapping(value = "/cart/empty", method = RequestMethod.POST)
  public String emptyCart(@ModelAttribute("cartForm") CartForm formData, Model model) {

    Cart cart = cartService.emptyCart();
    session.setAttribute("cart", cart);
    session.setAttribute("cart_size", cart.getCartItems().size());
    return "redirect:/";
  }
}

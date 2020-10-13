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
import demo.model.CartItem;
import demo.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

  @Autowired private HttpSession session;
  @Autowired private ProductService productService;

  public Cart getCart() {
    return (Cart) Optional.ofNullable(session.getAttribute("cart")).orElseGet(Cart::new);
  }

  // Empty cart and reset values in session
  public Cart emptyCart() {
    Cart cart = new Cart();
    List<CartItem> items = new ArrayList<CartItem>();
    cart.setCartItems(items);
    return cart;
  }

  /**
   * Check if a product is already added to the cart
   *
   * @param id
   * @param cart
   */
  public Optional<CartItem> exists(String id, List<CartItem> cart) {
    return cart.stream().filter(c -> c.getProductId().equals(id)).findAny();
  }

  /**
   * Create list of products from the cart items. This is more of a workaround to cater to what's
   * expected to show up in the ftl templates
   *
   * @param session
   * @param cart
   */
  public List<Product> getSelectedProducts(HttpSession session, Cart cart) {
    List<Product> selectedProducts = new ArrayList<>();
    for (CartItem item : cart.getCartItems()) {
      selectedProducts.add(productService.findProductById(item.getProductId()).get());
    }
    return selectedProducts;
  }

  /**
   * Create new cart item based on the user selection
   *
   * @param selectedProduct
   * @param selectedQty
   * @param cart
   */
  public CartItem newCartItem(String selectedProduct, int selectedQty, Cart cart) {
    CartItem cartItem = new CartItem();
    cartItem.setProductId(selectedProduct);
    cartItem.setQuantity(selectedQty);
    cartItem.setCart(cart);
    return cartItem;
  }
}

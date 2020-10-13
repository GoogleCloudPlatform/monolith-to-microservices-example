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
import demo.service.AdsService;
import demo.service.ProductService;
import demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller to handle flow after user makes a product selection * */
@Controller
public class ProductController {

  @Autowired private ProductService productService;
  @Autowired private RecommendationService recommendationService;
  @Autowired private AdsService adsService;

  /**
   * Based on user selection, load product details, recommended matching products and relevant ads
   *
   * @param id product-id
   * @param model
   * @return filled model with relevant values
   */
  @RequestMapping("/product/{id}")
  public String getProductById(@PathVariable String id, Model model) throws Exception {

    Product prod = productService.findProductById(id).get();

    model.addAttribute("prod", prod);
    model.addAttribute("recommend", recommendationService.getRecommendedProducts((Product) prod));
    model.addAttribute("ad", adsService.getRecommendedAd((Product) prod));

    return "product";
  }
}

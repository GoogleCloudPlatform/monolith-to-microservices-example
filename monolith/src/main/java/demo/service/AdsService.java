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

import com.google.common.collect.ImmutableListMultimap;
import demo.model.Ad;
import demo.model.Product;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class AdsService {

  private static final ImmutableListMultimap<String, Ad> adsMap = createAdsMap();
  private static final Random random = new Random();

  /**
   * Get recommended ad based on product selection
   *
   * @param product user selected product
   * @return ad
   */
  public Ad getRecommendedAd(Product product) {
    List<String> categories = product.getCategories();
    List<Ad> ads = adsMap.get(categories.get(random.nextInt(categories.size())));
    return ads.get(random.nextInt(ads.size()));
  }

  private static ImmutableListMultimap<String, Ad> createAdsMap() {
    Ad camera =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/2ZYFJ3GM2N")
            .setText("Film camera for sale. 50% off.")
            .build();
    Ad lens =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/66VCHSJNUP")
            .setText("Vintage camera lens for sale. 20% off.")
            .build();
    Ad recordPlayer =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/0PUK6V6EV0")
            .setText("Vintage record player for sale. 30% off.")
            .build();
    Ad bike =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/9SIQT8TOJO")
            .setText("City Bike for sale. 10% off.")
            .build();
    Ad baristaKit =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/1YMWWN1N4O")
            .setText("Home Barista kitchen kit for sale. Buy one, get second kit for free.")
            .build();
    Ad airPlant =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/6E92ZMYYFZ")
            .setText("Air plants for sale. Buy two, get third one for free.")
            .build();
    Ad terrarium =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/L9ECAV7KIM")
            .setText("Terrarium for sale. Buy one, get second one for free.")
            .build();

    return ImmutableListMultimap.<String, Ad>builder()
        .putAll("photography", camera, lens)
        .putAll("vintage", camera, lens, recordPlayer)
        .put("cycling", bike)
        .put("cookware", baristaKit)
        .putAll("gardening", airPlant, terrarium)
        .build();
  }
}

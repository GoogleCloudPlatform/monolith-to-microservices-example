# Copyright 2020 Google LLC. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import json


# Loop through products to find matching products by category
def findMatchingProducts(categories):
  list = []
  with open('products.json') as products:
    data = json.load(products)
    for key, value in enumerate(d.items() for d in data):
      match = any(item in data[key]['categories'] for item in categories)
      if match is True:
        list.append(data[key]['id'])

  return list

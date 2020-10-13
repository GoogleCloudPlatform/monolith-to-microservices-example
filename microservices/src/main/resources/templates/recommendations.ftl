<#--
Copyright 2020 Google LLC. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<h5 class="text-muted">Products you might like</h5>
<div class="row my-2 py-3">
   <#list recommend as rec>
   <div class="col-sm-6 col-md-4 col-lg-3">
      <div class="card mb-3 box-shadow">
         <a href="/product/${rec.id}">
         <img class="card-img-top border-bottom" alt =""
            style="width: 100%; height: auto;"
            src="${rec.picture}">
         </a>
         <div class="card-body text-center py-2">
            <small class="card-title text-muted">
            ${rec.name}
            </small>
         </div>
      </div>
   </div>
   </#list>
</div>


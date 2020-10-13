<#-- Copyright 2020 Google LLC. All rights reserved.
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
<#include "header.ftl">
<main role="main">
   <section class="jumbotron text-center mb-0"
   {{ with $.banner_color }}
   style="background-color: {{.}};"
   {{ end }}
   >
   <div class="container">
      <h1 class="jumbotron-heading">
         One-stop for Fashion &amp; Style Online
      </h1>
      <p class="lead text-muted">
         Tired of mainstream fashion ideas, popular trends and
         societal norms? This line of lifestyle products will help
         you catch up with the trend and express your
         personal style. Start shopping hip and vintage items now!
      </p>
   </div>
   </section>
   <div class="py-5 bg-light">
      <div class="container">
         <div class="row">
            <#list products as prod>
            <div class="col-md-4">
               <div class="card mb-4 box-shadow">
                  <a href="product/${prod.id}">
                  <img class="card-img-top" alt =""
                     style="width: 100%; height: auto;"
                     src="${prod.picture}">
                  </a>
                  <div class="card-body">
                     <h5 class="card-title">
                        ${prod.name}
                     </h5>
                     <div class="d-flex justify-content-between align-items-center">
                        <div class="btn-group">
                           <a href="product/${prod.id}">
                           <button type="button" class="btn btn-sm btn-outline-secondary">Buy</button>
                           </a>
                        </div>
                        <small class="text-muted">
                        ${prod.priceUsd}
                        </strong>
                        </small>
                     </div>
                  </div>
               </div>
            </div>
            </#list>
         </div>
         <div class="row">
         </div>
      </div>
   </div>
</main>
<#include "footer.ftl">


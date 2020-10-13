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
<#include "header.ftl">
<main role="main">
   <div class="py-5">
      <div class="container bg-light py-3 px-lg-5">
         <div class="row mt-5 py-2">
            <div class="col">
               <h3>
                  Your order is complete!
               </h3>
               <p>
                  Order Confirmation ID: <strong>${orderId}</strong>
               </p>
               <a class="btn btn-primary" href="/" role="button">Browse other products &rarr; </a>
            </div>
         </div>
         <hr/>
         <div class="row mt-5 py-2">
            <#if recommend??>
            <#include "recommendations.ftl">
            </#if>
         </div>
      </div>
   </div>
</main>
<#include "footer.ftl">


/* Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const ZB = require('zeebe-node')

;
(async () => {
  const zbc = new ZB.ZBClient(process.env.ZEEBE_SERVICE_ENDPOINT);
  zbc.createWorker('test-worker', 'make-payment', makePayment);
  zbc.createWorker('test-worker', 'cancel-payment', cancelPayment);
})()


/**
 * If successful, trigger "process-order" event in the workflow.
 * If fails, trigger "cancel-order" event in the workflow.
 * Handlers in the upstream frontend service listen for these events.
 */
function makePayment(job, complete) {
  console.log('Inside make payment');
  let params;

  // Dummy payment
  params = updatePaymentStatus(job, true);
  console.log('Payment successful');
  complete.success(params);
}

function updatePaymentStatus(job, status) {
  return Object.assign({}, job.variables, {paymentsuccess: status})
}

function cancelPayment(job, complete) {
  console.log(
      'Inside cancel payment. Placeholder for payment canceled cleanup.');
  complete.success();
}

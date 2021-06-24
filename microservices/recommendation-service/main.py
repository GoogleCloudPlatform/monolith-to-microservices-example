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

import os
import grpc
from concurrent import futures
import time

import demo_pb2
import demo_pb2_grpc
import recommend

from opencensus.trace.tracer import Tracer
from opencensus.ext.stackdriver import trace_exporter as stackdriver_exporter
from opencensus.trace.propagation import b3_format
from opencensus.trace import samplers
from opencensus.trace import span_context

# [START register_SD_exporter]
stackdriverExporter = stackdriver_exporter.StackdriverExporter(
    project_id=os.environ.get('GCP_PROJECT_ID'))
# [END register_SD_exporter]

# Define server functions, derived from demo_pb2_grpc.RecommendationServiceServicer
class RecommendationServiceServicer(demo_pb2_grpc.RecommendationServiceServicer
                                   ):

  # Get recommended products based on selected product received from upstream
  # frontend service. Also instrument a nested tracing span based on the context
  # received from the upstream frontend service.
  def ListRecommendations(self, request, context):

    metadata = context.invocation_metadata()
    metadata_dict = {}
    for c in metadata:
      metadata_dict[c.key] = c.value

    # [START extract_context]
    ctx = b3_format.B3FormatPropagator().from_headers(metadata_dict)
    tracer = Tracer(
        sampler=samplers.ProbabilitySampler(rate=1.0),
        span_context=ctx,
        exporter=stackdriverExporter)
    with tracer.span(name='Recommendation Backend') as span:
      response = demo_pb2.ListRecommendationsResponse()
       # [END extract_context]
      response.product_ids[:] = recommend.findMatchingProducts(
          request.product_category)
      return response
  
  def HealthCheck(self, request, context):
    tracer = Tracer(sampler=samplers.AlwaysOffSampler())
    response = demo_pb2.HealthStatusResponse()
    response.status = 'ok'
    return response


def main():
  server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
  demo_pb2_grpc.add_RecommendationServiceServicer_to_server(
      RecommendationServiceServicer(), server)

  print('Starting server')
  server.add_insecure_port('[::]:50051')
  server.start()

  try:
    while True:
      time.sleep(86400)
  except KeyboardInterrupt:
    server.stop(0)


if __name__ == '__main__':
  main()

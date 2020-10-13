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

package demo.config.recommendation;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.MethodDescriptor;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;
import io.opencensus.trace.propagation.TextFormat.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Intercept outgoing gRPC calls and add inject trace context via <a
 * href="#{@B3-headers}">{@https://github.com/openzipkin/b3-propagation}</a>. The trace context is
 * extracted by services to create nested spans.
 */
@Component
public class GrpcClientInterceptor implements ClientInterceptor {
  @Autowired private Tracer tracer;
  private static final TextFormat B3_FORMAT = Tracing.getPropagationComponent().getB3Format();
  private static final Setter<Metadata> METADATA_SETTER = new B3Setter();

  private static class B3Setter extends Setter<Metadata> {
    @Override
    public void put(Metadata carrier, String key, String value) {
      carrier.put(Key.of(key, Metadata.ASCII_STRING_MARSHALLER), value);
    }
  }

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
    return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
      // [START inject]
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        B3_FORMAT.inject(tracer.getCurrentSpan().getContext(), headers, METADATA_SETTER);
        // [END inject]
        super.start(
            new SimpleForwardingClientCallListener<RespT>(responseListener) {
              @Override
              public void onHeaders(Metadata headers) {
                super.onHeaders(headers);
              }
            },
            headers);
      }
    };
  }
}

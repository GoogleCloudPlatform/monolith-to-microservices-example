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
package main

import (
    "encoding/json"
    "fmt"
    "io/ioutil"
    "log"
    "net/http"
    "os"

    "contrib.go.opencensus.io/exporter/stackdriver"
    "go.opencensus.io/plugin/ochttp"
    "go.opencensus.io/plugin/ochttp/propagation/b3"
    "go.opencensus.io/stats/view"
    "go.opencensus.io/trace"
)

type ProductCategory struct {
    Category string
}

/**
 * Fetch relevant ads based on the request received from the upstream
 * frontend service.
 */
func serveAd(w http.ResponseWriter, r * http.Request) {
    var p ProductCategory
    var results[] map[string] interface {}

    // [START nested_trace_span]
    ctx: = r.Context()
    HTTPFormat: = & b3.HTTPFormat {}
    sc, ok: = HTTPFormat.SpanContextFromRequest(r)
    if ok {
        _, span: = trace.StartSpanWithRemoteParent(ctx, "Ads Backend", sc)
        defer span.End()
    }
    // [END nested_trace_span]
  
    // Fetch ad
    decoder: = json.NewDecoder(r.Body)
    err: = decoder.Decode( & p)
    if err != nil {
        panic(err)
    }

    jsonFile, err: = os.Open("ads.json")
    if err != nil {
        fmt.Println(err)
    }
    defer jsonFile.Close()

    byteValue, _: = ioutil.ReadAll(jsonFile)
    json.Unmarshal([] byte(byteValue), & results)
    for key, result: = range results {
        _ = key
        ads: = result["ads"].(map[string] interface {})
        if result["name"] == p.Category {
	    // TBD: Resorting to string for demonstrating functionality
            str: = fmt.Sprintf("%v", ads["redirecturl"]) + ":" + fmt.Sprintf("%v", ads["text"])
            w.Write([] byte(str))
        }
    }
}

func main() {
    // [START register_SD_exporter]
    exporter, err: = stackdriver.NewExporter(stackdriver.Options {
        ProjectID: os.Getenv("GCP_PROJECT_ID"),
    })

    if err != nil {
        log.Fatal(err)
    }
    trace.RegisterExporter(exporter)
    trace.ApplyConfig(trace.Config {
        DefaultSampler: trace.AlwaysSample()
    })
    // [END register_SD_exporter]

    // Handle incoming request
    mux: = http.NewServeMux()
    mux.HandleFunc("/servead", serveAd)
    h: = & ochttp.Handler {
        Handler: mux
    }
    if err: = view.Register(ochttp.DefaultServerViews...);
    err != nil {
        log.Fatal("failed to register default views")
    }

    log.Println("Starting server on :8001...")
    log.Fatal(http.ListenAndServe(":8001", h))
}

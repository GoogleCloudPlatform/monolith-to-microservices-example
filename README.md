# Refactoring a monolithic application to microservices
This repository contains the code used in the tutorials [Inter-service communication in a microservices setup](https://cloud.google.com/architecture/microservices-architecture-interservice-communication) and [Distributed tracing in a microservices application](https://cloud.google.com/architecture/microservices-architecture-distributed-tracing).

This tutorial series uses the example of a dummy ecommerce application and walks through the steps of converting the monolithic application to microservices. The tutorial series demonstrates microservices development using [Strangler Pattern](https://martinfowler.com/bliki/StranglerFigApplication.html) for checkout and payment flows in the example ecommerce application. Other details covered include distributed transactions spanning across microservices boundaries, implementing compensating transactions using [Sagas](https://microservices.io/patterns/data/saga.html), isolating microservices using gRPC, and OpenCensus/Stackdriver based distributed tracing for a polyglot application running different protocols (HTTP and gRPC).

The examples included in the tutorial series come with pre-configured images for easy installation. However, if you wish to change the code and rebuild the container images, run the following:
```
cd <directory-where-Dockerfile-is-located>
gcloud builds submit --tag gcr.io/<project-id>/<image-url>
```
For example,
```
cd microservices/recommendation-services
gcloud builds submit --tag gcr.io/<project-id>/ecomm-recommendation
```
The image-urls used in the tutorials can be found in the respective deployment files under monolith/ and microservices/kube/ directories. For more details on running gcloud builds, read [building container images on Google
Cloud](https://cloud.google.com/cloud-build/docs/building/build-containers#use-dockerfile). If you choose to run your container images, make sure that the project-id is changed accordingly in the respective deployment files.

## Disclaimer
This is not an officially supported Google product.

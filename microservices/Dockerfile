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

FROM maven:3.5-jdk-8 AS MAVEN_TOOL_CHAIN
WORKDIR /tmp
COPY pom.xml ./
COPY src ./src
RUN mvn dependency:go-offline -B
RUN mvn package

FROM openjdk:8-jre
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/ecomm-1.0-SNAPSHOT.jar /tmp/
COPY src/main/resources/application.properties /tmp/
WORKDIR /tmp
CMD ["java","-jar","ecomm-1.0-SNAPSHOT.jar", "--spring.config.location=file:application.properties"]

FROM adoptopenjdk/openjdk11:slim

WORKDIR "/discography"

RUN mkdir project
COPY ["project/assembly.sbt", "project/build.properties", "project/plugins.sbt", "./project/"]
COPY ["build.sbt", "./"]

ENV SBT_VERSION 1.2.8

RUN curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

RUN sbt update

COPY [".scalafmt.conf", "./"]
COPY ["app", "app"]
COPY ["conf", "conf"]
COPY ["public", "public"]
RUN sbt assembly


FROM adoptopenjdk/openjdk11:slim

WORKDIR "/discography"

COPY --from=0 /discography/target/scala-*/discography.jar .
CMD ["java", "-jar", "discography.jar"]

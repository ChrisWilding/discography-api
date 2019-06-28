FROM adoptopenjdk/openjdk11:slim

ENV SBT_VERSION 1.2.8
ENV SCALA_VERSION 2.12.8

RUN curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc

RUN curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

WORKDIR "/discography"

RUN mkdir project src
COPY [".scalafmt.conf", "build.sbt", "./"]
COPY ["project/assembly.sbt", "project/build.properties", "project/plugins.sbt", "./project/"]
RUN sbt update
COPY ["app", "app"]
COPY ["conf", "conf"]
COPY ["public", "public"]
RUN sbt assembly


FROM adoptopenjdk/openjdk11:slim

WORKDIR "/discography"

COPY --from=0 /discography/target/scala-*/discography.jar .
CMD ["java", "-jar", "discography.jar"]

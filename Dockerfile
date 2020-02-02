FROM hseeberger/scala-sbt:8u242_1.3.7_2.13.1

COPY build.sbt .
COPY project/ project/

RUN ["sbt", "updateIntellij"]

COPY . .

ENTRYPOINT ["sbt", "test"]

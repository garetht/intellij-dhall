FROM gradle:6.1-jdk as generate

COPY src/main/scala/org/intellij/plugins/dhall/_DhallLexer.flex src/main/scala/org/intellij/plugins/dhall/_DhallLexer.flex
COPY src/main/scala/org/intellij/plugins/dhall/Dhall.bnf src/main/scala/org/intellij/plugins/dhall/Dhall.bnf
COPY _ci/build.gradle build.gradle

RUN ["gradle", "build"]

FROM hseeberger/scala-sbt:8u242_1.3.7_2.13.1

COPY build.sbt .
COPY project/ project/

RUN ["sbt", "updateIntellij"]

COPY --from=generate /home/gradle/gen gen/
COPY . .

ENTRYPOINT ["sbt", "test"]

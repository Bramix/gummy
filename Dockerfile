FROM eclipse-temurin:17-jre-jammy

COPY ./target/gymmy-*.jar /service.jar

RUN addgroup spring && adduser --ingroup spring --disabled-password spring
USER spring

ENTRYPOINT ["java","-jar","/service.jar"]
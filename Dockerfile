FROM eclipse-temurin:21-jdk

COPY ./target/gymmy-*.jar /service.jar

RUN addgroup spring && adduser --ingroup spring --disabled-password spring
USER spring

ENTRYPOINT ["java","-jar","/service.jar"]
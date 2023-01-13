FROM maven:latest as maven

WORKDIR .
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run

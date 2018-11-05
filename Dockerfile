FROM openjdk:8u121-jre-alpine
MAINTAINER Morten Frank

WORKDIR /var/dropwizard


ENV MYSQL_ADRESS <INSERT_MYSQL_ADDRESS>
ENV MYSQL_DATABASE <INSERT_DATABASE_NAME>
ENV MYSQL_USER <INSERT_MYSQL_USER>
ENV MYSQL_PASSWORD <INSERT_MYSQL_PASSWORD>
ENV MYSQL_PORT <INSERT_MYSQL_PORT>
ENV API_USERNAME test
ENV API_PASSWORD YBNaKLms3HtaW2htwwYeRb8y
ENV DROPWIZARD_PORT 8081
ENV DROPWIZARD_ADMIN_PORT 8082

ADD target/soeofficer-0.0.1.jar /var/dropwizard/soeofficer-0.0.1.jar
ADD src/main/resources/config.yml /var/dropwizard/config.yml
ADD scripts/entrypoint.sh /entrypoint.sh

RUN chmod 755 /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
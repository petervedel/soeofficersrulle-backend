FROM openjdk:8u121-jre-alpine
MAINTAINER Morten Frank

WORKDIR /var/dropwizard


ENV MYSQL_ADRESS nordkerndk.csk1n1qcyjd7.eu-central-1.rds.amazonaws.com
ENV MYSQL_DATABASE soeofficer
ENV MYSQL_USER nordkern
ENV MYSQL_PASSWORD x9ydmcArUNhN=&YXrac
ENV MYSQL_PORT 3306
ENV API_USERNAME test
ENV API_PASSWORD YBNaKLms3HtaW2htwwYeRb8y
ENV DROPWIZARD_PORT 8081
ENV DROPWIZARD_ADMIN_PORT 8082

ADD target/soeofficer-0.0.1.jar /var/dropwizard/soeofficer-0.0.1.jar
ADD src/main/resources/config.yml /var/dropwizard/config.yml
ADD scripts/entrypoint.sh /entrypoint.sh

RUN chmod 755 /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
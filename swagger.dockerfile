FROM swaggerapi/swagger-ui:latest
MAINTAINER Morten Frank

WORKDIR /var/swagger

ADD target/swagger-ui/swagger.json /var/swagger/swagger.json
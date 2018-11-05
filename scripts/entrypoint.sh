#!/bin/sh
java -jar /var/dropwizard/soeofficer-0.0.1.jar server config.yml

exec "$@"
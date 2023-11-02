#!/usr/bin/env bash

mvn clean package && \
asadmin deploy --force target/persistence.war


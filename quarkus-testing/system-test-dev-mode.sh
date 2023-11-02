#!/bin/bash
set -euo pipefail

# Function to run on exit
cleanup() {
  echo "Shutting down..."
  popd # Return to the initial directory
  docker-compose -f system-test-dev-mode.yml down
}

# Trap EXIT signal to run the cleanup function
trap cleanup EXIT

# Save the current directory and change to the coffee-shop directory
#Perform Maven build and Docker build
pushd "${0%/*}"/coffee-shop
mvn clean package -Dquarkus.package.type=mutable-jar
docker build -t coffee-shop:mutable .

# Return to the initial directory and start docker-compose
popd
docker-compose -f system-test-dev-mode.yml up -d

# Change to the coffee-shop directory for remote development
pushd "${0%/*}"/coffee-shop
mvn quarkus:remote-dev \
  -Dquarkus.live-reload.url=http://localhost:8080 \
  -Dquarkus.live-reload.password=123 \
  -Dquarkus.package.type=mutable-jar

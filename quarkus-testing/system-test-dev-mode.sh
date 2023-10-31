#!/bin/bash
set -euo pipefail

# Function to run on exit
cleanup() {
  echo "Shutting down..."
  cd ..
  docker-compose -f system-test-dev-mode.yml down
}

# Trap EXIT signal to run the cleanup function
trap cleanup EXIT

# Go to the coffee-shop directory
cd "${0%/*}"/coffee-shop || exit

# Build the application
mvn clean package -Dquarkus.package.type=mutable-jar

# Build the Docker image
docker build -t coffee-shop:mutable .

# Return to the previous directory
cd ..

# Start up the services using docker-compose
docker-compose -f system-test-dev-mode.yml up -d

# Go back to the coffee-shop directory
cd "${0%/*}"/coffee-shop || exit

# Start the Quarkus remote development
mvn quarkus:remote-dev \
  -Dquarkus.live-reload.url=http://localhost:8080 \
  -Dquarkus.live-reload.password=123 \
  -Dquarkus.package.type=mutable-jar

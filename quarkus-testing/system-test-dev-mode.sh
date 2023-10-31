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

cd "${0%/*}"/coffee-shop || exit
mvn clean package -Dquarkus.package.type=mutable-jar
docker build -t coffee-shop:mutable .

cd ..
docker-compose -f system-test-dev-mode.yml up -d

cd "${0%/*}"/coffee-shop || exit
mvn quarkus:remote-dev \
  -Dquarkus.live-reload.url=http://localhost:8080 \
  -Dquarkus.live-reload.password=123 \
  -Dquarkus.package.type=mutable-jar

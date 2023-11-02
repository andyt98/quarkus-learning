#!/bin/bash

POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
DB_NAME=postgres
CONTAINER_NAME=customerdb
VOLUME_NAME=customerdb


# Function to check if a Docker container is running
is_container_running() {
  docker container inspect "$1" >/dev/null 2>&1
}


# Check if PostgreSQL container is already running
if ! is_container_running "$CONTAINER_NAME"; then
  echo "Starting PostgreSQL container..."
  docker run -d \
    -p 5432:5432 \
    --name $CONTAINER_NAME \
    -e POSTGRES_USER=$POSTGRES_USER \
    -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
    -e POSTGRES_DB=$DB_NAME \
    -v $VOLUME_NAME:/var/lib/postgresql/data \
    postgres:15.4
else
  echo "PostgreSQL container is already running."
fi

# Wait for PostgreSQL container to start
if ! is_container_running "$CONTAINER_NAME"; then
  echo "Waiting for PostgreSQL container to start..."
  until is_container_running "$CONTAINER_NAME"; do
    sleep 1
  done
  echo "PostgreSQL container is now running."
fi



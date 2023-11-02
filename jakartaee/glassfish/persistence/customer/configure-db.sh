#!/bin/bash

POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
DB_NAME=postgres

CONTAINER_NAME=postgres
POOL_NAME=postgresPool

DOMAIN_NAME=demo
DOMAIN_ADMIN=admin
DOMAIN_ADMIN_PASS_FILE=$GLASSFISH_HOME/glassfish/domains/password.properties
JDBC_DRIVER_JAR=./postgresql-42.6.0.jar

# Function to check if a Docker container is running
is_container_running() {
  docker container inspect "$1" >/dev/null 2>&1
}

# Function to check if a GlassFish domain is running
is_domain_running() {
  asadmin list-domains | grep -q "$1"
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
    -v data:/var/lib/postgresql/data \
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

# Start GlassFish domain if not running
if ! is_domain_running "$DOMAIN_NAME"; then
  asadmin start-domain --debug $DOMAIN_NAME
else
  echo "GlassFish domain is already running."
fi

# Add JDBC driver library if not added
if ! asadmin list-libraries --user $DOMAIN_ADMIN --passwordfile $DOMAIN_ADMIN_PASS_FILE | grep -q "$JDBC_DRIVER_JAR"; then
  asadmin --user $DOMAIN_ADMIN --passwordfile $DOMAIN_ADMIN_PASS_FILE add-library $JDBC_DRIVER_JAR
else
  echo "JDBC driver library is already added."
fi


# Create JDBC Connection Pool if not created
if ! asadmin list-jdbc-connection-pools --user $DOMAIN_ADMIN --passwordfile $DOMAIN_ADMIN_PASS_FILE | grep -q "$POOL_NAME"; then
  echo "Creating JDBC Connection Pool..."
  asadmin --user $DOMAIN_ADMIN --passwordfile $DOMAIN_ADMIN_PASS_FILE \
    create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource \
    --restype javax.sql.ConnectionPoolDataSource \
    --property user=$POSTGRES_USER:password=$POSTGRES_PASSWORD:serverName=localhost:portNumber=5432:databaseName=$DB_NAME \
    $POOL_NAME
else
  echo "JDBC Connection Pool is already created."
fi

# Create JDBC Resource if not created
if ! asadmin list-jdbc-resources --user $DOMAIN_ADMIN --passwordfile $DOMAIN_ADMIN_PASS_FILE | grep -q "jdbc/$DB_NAME"; then
  echo "Creating JDBC Resource..."
  asadmin --user $DOMAIN_ADMIN --passwordfile $DOMAIN_ADMIN_PASS_FILE \
    create-jdbc-resource --connectionpoolid $POOL_NAME jdbc/$DB_NAME
else
  echo "JDBC Resource is already created."
fi

# Restart GlassFish Server
echo "Restarting GlassFish Server..."
asadmin restart-domain $DOMAIN_NAME

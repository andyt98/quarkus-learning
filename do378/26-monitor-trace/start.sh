#!/bin/bash
set -euo pipefail

# Function to run on exit
cleanup() {
  echo "Shutting down..."
  kill "$SOLVER_PID" "$ADDER_PID" "$MULTIPLIER_PID"
  sleep 2
  echo "All services terminated"
}

# Trap EXIT signal to run the cleanup function
trap cleanup EXIT

pushd "${0%/*}"/solver
mvn clean quarkus:dev -Ddebug=5005 &
SOLVER_PID=$!
popd

pushd "${0%/*}"/adder
mvn clean quarkus:dev -Ddebug=5006 &
ADDER_PID=$!
popd

pushd "${0%/*}"/multiplier
mvn clean quarkus:dev -Ddebug=5007 &
MULTIPLIER_PID=$!
popd

sleep 1d

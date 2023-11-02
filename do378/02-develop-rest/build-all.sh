#!/bin/bash
set -euo pipefail
cd "${0%/*}"

pushd expense-service/
  echo building expense-service
  mvn clean package
popd

pushd expense-client/
  echo building expense-client
  mvn clean package
popd

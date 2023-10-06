#!/bin/bash
set -euo pipefail
cd "${0%/*}"

pushd coffee-shop/
  echo building coffee-shop
  mvn clean package
  docker build -t coffee-shop .
popd

pushd barista/
  echo building barista
  mvn clean package
  docker build -t barista .
popd

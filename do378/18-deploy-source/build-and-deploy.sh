#!/bin/bash
oc new-project deploy-source
oc create configmap expense-client-config --from-literal EXPENSE_SVC=http://expense-service:80 -o yaml

set -euo pipefail
cd "${0%/*}"

pushd expense-service/
  mvn clean package -Dquarkus.kubernetes.deploy=true
popd

pushd expense-client/
  mvn clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.openshift.route.expose=true
popd

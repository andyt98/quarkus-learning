oc new-project tolerance-health

mvn clean package \
-Dquarkus.kubernetes.deploy=true \
-Dquarkus.openshift.expose=true \
-DskipTests

oc get pods -w

# oc describe pod quarkus-calculator-1-POD_SUFFIX
# curl http://quarkus-calculator-tolerance-health.apps.ocp4.example.com/crash

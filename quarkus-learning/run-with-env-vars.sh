export EXAMPLE_GREETING="Hi Mom!" \
 && mvn clean package \
 && java  -jar target/quarkus-app/quarkus-run.jar
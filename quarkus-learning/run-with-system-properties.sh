mvn clean package \
&& java -Dexample.greeting="Hello Quarkus!" -jar target/quarkus-app/quarkus-run.jar
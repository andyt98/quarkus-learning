mvn clean package
docker build -f src/main/docker/Dockerfile.jvm -t docker.io/andyt98/expense-quarkus .
docker push docker.io/andyt98/expense-quarkus

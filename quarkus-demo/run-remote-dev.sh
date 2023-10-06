docker run -it -e QUARKUS_LAUNCH_DEVMODE=true \
 --network quarkus-demo_dkrnet \
  -p 8080:8080 quarkus-demo

# mvn quarkus:remote-dev
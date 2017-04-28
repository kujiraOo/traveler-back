FROM library/postgres:9.5

ADD init.sql /docker-entrypoint-initdb.d/

FROM java:8

# Install maven
RUN apt-get update
RUN apt-get install -y maven

WORKDIR /code

# Add configuration
ADD configuration.yml /code/configuration.yml

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

ADD example.keystore /code/example.keystore

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN ["mvn", "package"]

EXPOSE 4567

RUN ls target
CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/traveler-back-1.0-SNAPSHOT.jar", "server", "configuration.yml"]

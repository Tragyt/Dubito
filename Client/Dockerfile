FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY Client ./client
COPY common ./common

RUN javac -d out \
    $(find common/src -name "*.java") \
    $(find client/src -name "*.java")
CMD ["java", "-cp", "out", "ClientMain"]
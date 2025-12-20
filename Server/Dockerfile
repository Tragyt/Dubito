FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY Server ./server
COPY common ./common

RUN javac -d out $(find common/src -name "*.java")
RUN javac -d out $(find server/src -name "*.java")

CMD ["java", "-cp", "out", "server.ServerMain"]
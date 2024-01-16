FROM eclipse-temurin:17.0.8_7-jre-jammy
LABEL authors="adamdrzewiecki"
COPY build/libs/drug-applications-server-0.0.1-SNAPSHOT.jar drug-applications-server.jar
ENTRYPOINT ["sh", "-c", "java -jar /drug-applications-server.jar"]
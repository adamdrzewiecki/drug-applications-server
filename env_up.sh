#!/bin/bash

echo "Starting all containers"

docker compose --profile all up mongodb -d

./gradlew clean assemble
image_name="docker.example.org/drug-applications-server"
opts="--no-cache"

DOCKER_BUILDKIT=1 docker build $opts -t $image_name:latest .
docker compose --profile all up drug-applications-server -d
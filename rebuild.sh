#!/bin/bash

echo "Rebuilding drug-applications-server..."

./gradlew clean assemble
image_name="docker.example.org/drug-applications-server"
opts="--no-cache"

DOCKER_BUILDKIT=1 docker build $opts -t $image_name:latest .
docker compose --profile all up drug-applications-server -d --remove-orphans
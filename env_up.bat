ECHO "Starting all containers"

docker compose --profile all up mongodb -d

call .\gradlew clean assemble
SET image_name="docker.example.org/drug-applications-server"
SET opts="--no-cache"

docker build %opts% -t %image_name%:latest .
docker compose --profile all up drug-applications-server -d

ECHO Done
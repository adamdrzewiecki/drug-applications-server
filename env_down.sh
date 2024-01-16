#!/bin/bash

echo "Shutting down whole environment"
docker compose --profile all down

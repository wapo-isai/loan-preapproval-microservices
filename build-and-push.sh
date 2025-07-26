#!/bin/bash

DOCKER_USERNAME="wapinho2016"
SERVICES=(
  "application-service"
  "loan-evaluation-service"
  "notification-service"
  "user-service"
)

set -e

for SERVICE in "${SERVICES[@]}"; do
  echo "----------------------------------------"
  echo "Building and pushing $SERVICE"
  echo "----------------------------------------"

  cd "$SERVICE"

  # Build with Gradle
  echo "Running ./gradlew build for $SERVICE..."
  ./gradlew build -x test

  # Docker image name
  IMAGE_NAME="$DOCKER_USERNAME/$SERVICE:latest"
  echo "Building Docker image $IMAGE_NAME..."
  docker build -t "$IMAGE_NAME" .

  echo "Pushing $IMAGE_NAME to Docker Hub..."
  docker push "$IMAGE_NAME"

  cd ..
done

echo "All services built and pushed successfully."

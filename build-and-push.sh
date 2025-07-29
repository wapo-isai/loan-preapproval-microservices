#!/bin/bash

DOCKER_USERNAME="wapinho2016"
SERVICES=(
  "application-service"
  "loan-evaluation-service"
  "notification-service"
  "user-service"
  "eureka-service"
  "gateway-service"
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
  IMAGE_NAME="$DOCKER_USERNAME/$SERVICE:3.0.0"
  echo "Building Docker image $IMAGE_NAME..."
  docker build --platform=linux/amd64 -t "$IMAGE_NAME" .

  echo "Pushing $IMAGE_NAME to Docker Hub..."
  docker push "$IMAGE_NAME"

  cd ..
done

echo "All services built and pushed successfully."

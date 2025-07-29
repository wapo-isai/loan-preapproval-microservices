#!/bin/bash

set -e

services=(
  "eureka-server:eureka-service"
  "gateway-server:gateway-service"
  "user-service:user-service-chart"
  "application-service:application-service-chart"
  "loan-evaluation-service:loan-evaluation-service-chart"
  "notification-service:notification-service-chart"
)

for service in "${services[@]}"; do
  IFS=":" read -r release chart <<< "$service"
  values_file="${chart}/values.yaml"

  echo "🚀 Deploying ${release} and waiting for it to be ready..."

  if helm status "$release" &>/dev/null; then
    helm upgrade "$release" "./${chart}" -f "$values_file" --wait
  else
    helm install "$release" "./${chart}" -f "$values_file" --wait
  fi

  echo "✅ ${release} deployed successfully!"
  echo
done

echo "🎉 All Helm charts deployed!"
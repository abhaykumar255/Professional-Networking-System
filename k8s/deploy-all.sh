#!/bin/bash

echo "Deploying Professional Networking System to Kubernetes..."

# Deploy infrastructure components first
echo "1. Deploying databases..."
kubectl apply -f connections-db.yaml
kubectl apply -f notifications-db.yaml
kubectl apply -f posts-db.yaml
kubectl apply -f users-db.yaml

echo "2. Deploying Redis and Kafka..."
kubectl apply -f redis.yaml
kubectl apply -f kafka.yaml

echo "3. Deploying ELK Stack..."
kubectl apply -f elasticsearch.yaml
kubectl apply -f logstash.yaml
kubectl apply -f kibana.yaml
kubectl apply -f filebeat.yaml

echo "4. Deploying Kafka UI..."
kubectl apply -f kafka-ui.yaml

echo "5. Deploying Zipkin..."
kubectl apply -f zipkin.yaml

echo "6. Deploying Discovery Server..."
kubectl apply -f discovery-server.yaml

echo "Waiting for infrastructure to be ready..."
sleep 60

# Deploy application services
echo "7. Deploying microservices..."
kubectl apply -f user-service.yaml
kubectl apply -f connection-service.yaml
kubectl apply -f posts-service.yaml
kubectl apply -f notification-service.yaml

echo "8. Deploying API Gateway..."
kubectl apply -f api-gateway.yaml

echo "Deployment complete! Checking pod status..."
kubectl get pods

echo "Services:"
kubectl get services

echo ""
echo "Access URLs (get external IPs with 'kubectl get services'):"
echo "- API Gateway: http://<api-gateway-external-ip>:9000"
echo "- Kibana: http://<kibana-external-ip>:5601"
echo "- Kafka UI: http://<kafka-ui-external-ip>:8080"
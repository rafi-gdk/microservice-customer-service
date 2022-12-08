mvn clean install
docker build -t customer-service -f ./docker-images/Dockerfile .
kubectl apply -f ./kubernetes/deployment.yaml
kubectl apply -f ./kubernetes/service.yaml
kubectl scale --replicas=0 deployment/customer-deployment
kubectl delete deployment/customer-deployment
kubectl delete service customer-deployment
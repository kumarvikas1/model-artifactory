# model-artifactory

Model Artifactory stores model in memory (This in real world should be replaced by repositories ideally JFrog OR S3).


Run Locally

```
minikube start

eval $(minikube docker-env)

docker build -t model-service:1 .

kubectl apply -f deployment.yaml

kubectl apply -f service.yaml

kubectl apply -f ingress.yaml
```

Test

Upload an artifact
```
curl --form file='@dummy_xgboost.dat' http://dev.xgboostdemo.com/model-service/upload/xgboost-demo
```

Download an artifact

```
curl http://dev.xgboostdemo.com/model-service/download-server/xgboost-demo
```

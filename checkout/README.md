# Checkout Microservice

## How to run ?

Create the containers:

```
cd docker
docker-compose up
```

Run application:
```
./gradlew bootRun
```

In Control Center you can see Kafka brokers, topics and etc: http://localhost:9021/

---

Creating a checkout:

```sh
curl --header "Content-Type: application/json" \
    --request POST \
    --data '{"firstName":"firstName1","lastName":"lastName1","email":"email1","address":"address1","complement":"complement1","country":"country1","state":"state1","cep":"cep1","saveAddress":true,"saveInfo":true,"paymentMethod":"paymentMethod1","cardName":"cardName1","cardNumber":"cardNumber1","cardDate":"cardDate1","cardCvv":"cardCvv1","products":["productA","productB"]}' \
    http://localhost:8080/v1/checkout
```


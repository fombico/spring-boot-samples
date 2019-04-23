# RabbitMQ Sample

A sample project that sends and receives messages to/from a rabbitmq server.

## Requires
- Lombok plugin and annotation processing - for logs
- RabbitMQ setup as below

## RabbitMQ Setup
1. Install rabbitmq
    ```
    brew install rabbitmq
    ```
1. Start rabbitmq
    ```
    brew services start rabbitmq
    ```
    RabbitMQ should be accessible via http://localhost:15672
1. Login with `guest/guest` and create the following queues:
    - fanout.queue1
    - fanout.queue2
    - topic.queue1
    - topic.queue2
1. Create the following exchanges:
    - fanout.exchange (with type as `fanout`)
    - topic.exchange (with type as `topic`)
1. Bind the exchanges to queues:
    - fanout.exchange to fanout.queue1, fanout.queue2
    - topic.exchange to topic.queue1 with routing key `*.important.*`
    - topic.exchange to topic.queue2 with routing key `#.error`

## Testing app
The default rabbitmq environment variables are:
```
spring.rabbitmq.host=127.0.0.1
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```
Override them in `application.properties` if required.

Startup app and make a `POST` to `http://localhost:8080/message` with a text body. e.g.
```
curl -X POST \
  http://localhost:8080/message \
  -H 'Content-Type: text/plain' \
  -d 'cookies and cream'
```

The app sends the message to the exchanges which are bound to queues. 
The app is bound to the queues and receives the messages back.

In the console, you should see:
```
Sending message: cookies and cream
Receive message from topic 1: cookies and cream
Receive message from fanout 1: cookies and cream
Receive message from fanout 2: cookies and cream
```

The app is configured to only send a message to topic.queue2 if there is the word `error` is present in the message.
```
curl -X POST \
  http://localhost:8080/message \
  -H 'Content-Type: text/plain' \
  -d 'error: something went wrong'
```
Then you will see something like:
```
Receive message from fanout 1: error: something went wrong
Receive message from fanout 2: error: something went wrong
Receive message from topic 2: error: something went wrong
Receive message from topic 1: error: something went wrong
```
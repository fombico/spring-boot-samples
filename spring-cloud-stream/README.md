# Spring Cloud Stream sample

A sample project using spring cloud stream and RabbitMQ/Kafka.
Two apps are present:
- spring-cloud-stream-output - takes a POST request and sends a message to the queue
- spring-cloud-stream-input - listens for a message from the queue

## Requirements
- Lombok plugin and annotation processing - for logs
- RabbitMQ and/or Kafka - for message queue

## RabbitMQ 

### Setup
1. Install rabbitmq
    ```
    brew install rabbitmq
    ```
1. Start rabbitmq
    ```
    brew services start rabbitmq
    ```
    RabbitMQ should be accessible via http://localhost:15672
 
### Run
1. Startup app and make a `POST` to `http://localhost:8080/book`. e.g.
   ```
   curl -X POST \
     http://localhost:8080/book \
     -H 'Content-Type: application/json' \
     -d '{
           "isbn":"978-0486272788",
           "title":"Hamlet",
           "author":"William Shakespeare"
     }'
   ```
1. See that a message is shown in the output application:
    ```
    Sending message: Book(isbn=978-0486272788, title=Hamlet, author=William Shakespeare)
    ```
1. See that a message is shown in the input application:
    ```
    Received message: Book(isbn=978-0486272788, title=Hamlet, author=William Shakespeare)
    Saved book: Book(isbn=978-0486272788, title=Hamlet, author=William Shakespeare)
    ```
    The `Store` class could be replaced with a data store (sql db, no-sql, etc)
1. See that a queue, named `sharedChannel` is automatically created in the RabbitMQ UI

## Kafka

### Setup
1. Install kafka
    ```
    brew install kafka
    ```
1. Start zookeeper (required for kafka)
    ```
    brew services start zookeeper
    ```
1. Start kafka
    ```
    brew services start kakfa
    ```

### Run
1. In the `build.gradle` for both projects, comment out the rabbit stream binder, and uncomment kafka 
    ```
    // implementation 'org.springframework.cloud:spring-cloud-stream-binder-rabbit'
       implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka'
    ```
1. Repeat steps 1~3 in the RabbitMQ Run section to make a POST and verify the output.
1. See that a queue, named `sharedChannel` is automatically created in Kafka
    ```
    > kafka-topics --zookeeper 127.0.0.1 --list
    sharedChannel
    ```


### Learnings
- Default config requires no changes in application properties
- `@EnableBinding({<Processor>.class})` allows your channels to be found and auto-creates the channels in Kafka/RabbitMQ
- `Processor` is a pre-defined processor that adds an `Input` and `Output` queue. To only add the input or output, use `Source` or `Sink` instead
- Message payloads can be typed, so that payloads are casted automatically:
    ```
    @StreamListener(CustomProcessor.CHANNEL)
    public void handleMessage(Message<Book> message) {
        ...
    }
    ``` 
- However, the `MessagingCollector` (used in tests) cannot cast automatically (as of writing)
    ```
    BlockingQueue<Message<?>> queue = messageCollector.forChannel(processor.outputChannel());
    ...
    String payload = (String) queue.poll().getPayload();
    assertThat(payload).isEqualTo(expectedPayload);
    ```
# AutoScout Application

This project contains the software framework for the AutoScout search user interface.

## Installation Instructions

To set up Kafka for this project, utilize Docker Compose for the installation.

For consuming messages, you can use the following Docker command:
```sh
docker exec -it kafka1 kafka-console-consumer --bootstrap-server localhost:9092 --topic autoScout24 --from-beginning
``` 

This will initiate a Kafka console consumer that connects to your local Kafka broker and consumes messages from the `autoScout24` topic right from the start.
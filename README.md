# Scalable-Server-Design-Using-Thread-Pools

The objective of this project is to balance the load on the server using threadpools. Thread creation is an expensive process, involving time and resources, so instead of spawning a thread for each message received by the server, it is ideal to use a threadpool that will execute the tasks. 

A Client connects to a server and maintains active connection with the server. It sends messages at a certain message rate. Server calculates throughput, mean per client messages processed/second and the standard deviation per client messages processed/second.

Threadpools are used to manage
1) Manage incoming network connections
2) Manage active network connections
3) Receive and Send data over these network connection.

### Prerequisites

Java 1.8


## Components

The project consists of two components
1) Server  - The server component consists of Server, ConnectionClass and ThreadpoolManager classes.
2) Client  - The server component consists of Client class.

### Defaults
The default message rate is once every second. The default threadpool size is 10. Default report printing is once every 10 seconds.

### Running a test

There is an additional class Start.class that will run a sample project assuming server as a localhost and uses port number 9091.
It will run the server and run it with three client connections.

To run this test project run the compiled code,
```
java Start
```

## Deployment

This server and client can be run anywhere with the command line arguments.
Server takes the following argument in order - port number and threadpool size.
Client takes the following argument in order - client name, server host ip address as a string, server port number and message rate/seconds.

```
java Server 9091 10

java Client mycomputer 123.123.123.123 9091 2
```

## What I learnt ?
I learnt lot of miniscule things that I didn't notice in my programming style. One of the mentionable things that I learnt is - polling for an object from a queue protected by a lock. This eases the sharing capability between threads.

## Authors

* **Sridhar Ramasamy**


# Autocompletion using Redis

## Getting Started

These instructions will get you a copy of the project up and running on your local machine. 

### Technologies Used
* **Java 1.8**
* **Spring MVC**
* **Gradle**
* **Redis**


### Installation
```sh
$ git clone https://anizzzzzzzz@bitbucket.org/anizzzzzzzz/redisautocompletion.git
$ cd RedisAutoCompletion/
```

### Installing Dependencies
##### On macOS and Linux:
``` 
./gradlew build --refresh-dependencies
``` 
##### On windows:
``` 
gradlew build --refresh-dependencies
```

### Pre-step
#### Installing and Running Redis:
[Redis](https://redis.io) is an in-memory data structure store, used as a database, cache and message broker. 
* Install redis on your system following [this link](https://redis.io/download).
* After completing the installation, start the redis server on your system by executing following command in terminal.
``` 
redis-server
```
* You can login into redis client by executing following command in terminal.
``` 
redis-cli
```
* Monitor all the operation executed in redis by
``` 
redis-cli monitor
```

### Running Web App
##### On macOS and Linux:
``` 
./gradlew bootRun
``` 
##### On windows:
``` 
gradlew bootRun
```
The project will run on localhost:8080

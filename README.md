# Cinematics

Cinematics is a simple REST microservice to store information about movies and images of ones. 
It is build with Spring Boot and mongoDb. To store images the application uses AWS S3 bucket.
It is necessary to create bucket on AWS S3 to run this microservice.

It can be build into a single .jar file using Gradle or using docker containers.
The docker-compose script run also frontend application from [here](https://github.com/ajtuss/cinematics-gui)
and seed the db with initial data.

## Getting Started

To clone this repository, execute the following in the command line:
```bash
$ git clone https://github.com/ajtuss/cinematics
```

To run the application copy the .env.template to .env and fill with your credentials to S3 bucket.
```bash
$ cp .env.template .env
```

## Build with Gradle
You can build the application with:
```bash
$ ./gradlew build
```
The .jar file will be in build/libs folder.

## Run with docker-compose
Run application with docker-compose:
```bash
$ docker-compose pull
$ docker-compose up -d
```
The docker-compose default download images from registry. To build them yourself execute:
```bash
$ docker-compose up -d --build
```

The running fronted should be visible on [localhost](http://localhost) and backend on [localhost:8080/api/movies](http://localhost:8080/api/movies)  


## Running tests

Run all backend tests with the following command in the root directory:
```bash
$ ./gradlew test
```

## Built With

* [Spring Boot 2.2.1](https://start.spring.io/)
* [Gradle](https://gradle.org/)
* [mongoDb](https://www.mongodb.com/)

## License

This project is licensed under the MIT License - see the [license details](https://opensource.org/licenses/MIT).


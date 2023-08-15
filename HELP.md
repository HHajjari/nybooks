# Book Search Application Documentation

Welcome to the documentation for the Book Search application built with Spring Boot. This application enables users to search for books by author and publication year using external APIs (NYTimes Books API and OpenLibrary). This document provides an overview of the application's structure, key components, API endpoints, authentication, and how to interact with the application.

## Table of Contents

- [Components](#components)
- [Endpoints](#endpoints)
- [Authentication](#authentication)
- [Swagger Documentation](#swagger-documentation)
- [Actuator and Health Monitoring](#health-monitoring)

## Components

The application is divided into several components that handle different aspects of book searching and retrieval:

- **BookController:** This Spring MVC controller handles incoming HTTP requests related to book searching. It utilizes the `SearchService` to retrieve book information based on author and year.

- **SearchService:** This service encapsulates the business logic for retrieving book information. It interacts with the `NYTBookService` to retrieve books by author and then filters results based on publication years.

- **NYTBookService:** This service interacts with the New York Times (NYT) API to retrieve book information by author. It processes API responses and maps them into `BookDto` objects. It also uses the `BookPublicationService` to retrieve publication years by ISBN.

- **OpenLibraryService:** This service implements the `BookPublicationService` interface and communicates with the OpenLibrary API to retrieve book publication years based on ISBNs.

- **BookPublicationService:** This interface defines the contract for retrieving book publication years by ISBN.`OpenLibraryService` implements this interface.

## Endpoints

The application exposes the following endpoint:

- **GET /me/books/list:** This endpoint allows users to search for books by author and publication year.
    - Parameters:
        - `author`: The author's name (required).
        - `year`: Publication year for filtering (optional).
    - Response: A list of book information in JSON format.

## Authentication

Access to the API endpoints requires basic authentication. Users need to provide valid credentials to access the API resources.

### Steps to Authenticate

1. Include basic authentication headers in your HTTP requests.
2. Provide the following credentials:
    - Username: `your-username`
    - Password: `your-password`
## User Authentication and Access Control

To interact with the API endpoints, user authentication is required. Users are assigned roles that determine their level of access to the API resources.

### User Credentials

User credentials, including usernames and passwords, are configured in the `application-[env].yaml` files. Users in roles `admin` or `operator1` are able to call the search api. You can also configure security configurations via `SecurityConfig.java`.

## Swagger Documentation

The Book Search application provides Swagger documentation that allows you to explore API documentation through `/swagger-ui/index.html`



## Health Monitoring

The Book Search application comes with Spring Boot Actuator, which provides insights into the application's internal state, health, and performance metrics. You can use these features to monitor the application's health, external API access, and response times.

### Enabling Actuator

Spring Boot Actuator is enabled by default in the application and is accessible through `/actuator/health`. You can access metrics related to the application's performance, including response times for external services through `/actuator/metrics`.

For example, you can access the following metrics:

#### `nytBook.api.call.duration`: 
Measures the duration of calls to the NYTimes API.
#### `openLibrary.api.call.duration`: 
Measures the duration of calls to the OpenLibrary API.

These metrics give insights into the performance of external API calls, helping you identify any bottlenecks or issues.
Users with the `admin` role have access to Actuator endpoints. This means that users with administrative privileges can monitor the application's health, metrics, and internal state by accessing the Actuator endpoints.


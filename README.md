# Edrone-Task
>A task to be performed as one of the stages of the recruitment interview.

## Table of contents
* [General](#general)
* [Techstack](#techstack)
* [Endpoints](#endpoints)
* [Status&config](#Status&config)
* [Contact](#Contact)

## General
This is an application to generate distinct permutation of given signs. \
As a user, one can choose minimal and maximal size of permutation, amount of permutations and possible chars. \
This is a web application hosted on Tomcat server, on default port 8082.\
Data is stored in local database and can be grabbed as a downloaded text file.\
POST requests are supported parallelly.


## Techstack:
- Java 17
- SQLite
- OrmLite
- Spring Boot
- Maven

## Endpoints
* /jobs - to get all recorded tasks from database.
* /jobs/{id} - to get specified job.
* /jobs/active - to get only tasks that are still being calculated.
* /jobs/get/{id} - to receive result of specified operation of permutation.
* (POST) /jobs/send - to send request with specified body

Body structure (JSON):
{
    "minLength" : {int},
    "maxLength" : {int},
    "stringAmount" : {long},
    "possibleChars" : {String}
}


## Status&config
This application is under construction.\
To make use of application, download the .jar file (located in target folder) and put it in separate folder.\
You may require proper java version to be installed.

## Contact

- Michał Mamla - https://github.com/bearyogi
- Email: mmichal1999@gmail.com

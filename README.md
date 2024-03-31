# Stellar Burgers Java API tests
    Automated API tests of the educational application "Stellar Burgers".
## Documentation
API [documentation](https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf) and [link](https://stellarburgers.nomoreparties.site/) to the Stellar Burgers educational app.
## Description
    Java 11 version. 
Libraries:
- JUnit v4.13.2;
- RestAssured v4.4.0;
- Allure v2.15.0;
- Aspectj v1.9.7;
- Gson v2.10.1;
## Launch
To run the autotest you need to:

1. Clone the repository using the command:
```sh
git clone git@github.com:GermanGri/Diplom_2.git
```
2. To run tests
```sh
mvn clean test
```
3. To create a report in Allure
```sh
mvn allure:report
```
## Completed tasks
1. Creating a user:
- create a unique user;
- create a user who is already registered;
- create a user and don't fill in one of the required fields.
2. User login:
- login under an existing user;
- login with incorrect login and password;
3. Changing user data:
- with authorization;
- without authorization;
4. Creating an order:
- with authorization;
- without authorization;
- with ingredients;
- without ingredients;
- with an incorrect ingredient hash.
5. Receiving orders from a specific user:
- authorized user;
- unauthorized user;
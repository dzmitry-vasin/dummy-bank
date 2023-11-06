# The Dummy Bank implementation

This is a home assignment task with a limited acceptance criteria.

Please take into account that there is no security and validation applied around API functionalities.

# Application startup

To run the application please use maven wrapper provided within the project root directory:

`./mvnw spring-boot:run`

The application will start on the 8080 port and the `/api` servlet context.

# Application testing

To run all the provided tests cases with code coverage verification you can use the following command:

`./mvnw clean verify`

To check code coverage report please visit `${project-root}/target/site/jacoco` directory.

# The API specification

There are 3 endpoints supported:

1. Retrieve client's accounts list

`GET /clients/${id}/accounts`

There are already defined list of client identifiers: "Bill Gates", "Elon Musk", "Jeff Bezos".

Please use these names as the variable path substitution for ${id} placeholder to fetch corresponding accounts.

2. Create money transfer transaction between existing accounts.
```
POST /transactions
{
  "fromAccount": {
    "externalId": ${fromAccountExternalId}
  },
  "toAccount": {
    "externalId": ${toAccountExternalId}
  },
  "amount": ${amount}
}
```

3. Tracking list of transactions made with the previous API method.

`GET /accounts/${id}/transactions`

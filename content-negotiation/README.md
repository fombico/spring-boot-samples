A sample project showing content negotiation with JSON and XML.

GET http://localhost:8080/v1/person
- `Accept` header can be `application/json` or `application/xml` and returns json or xml respectively

POST http://localhost:8080/v1/person
- `Content-Type` header can be `application/json` or `application/xml` and accepts json or xml respectively
- does some basic field validation

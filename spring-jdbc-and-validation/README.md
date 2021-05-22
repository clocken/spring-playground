# Spring-JDBC and Validation

Sample project showing a simple REST controller accepting a JSON array of domain objects and doing
custom [Java Bean Validation](https://beanvalidation.org/) on every array entry to filter candidates for merging into
DB.

* Domain objects / entities are located
  in [src/main/java/com/github/clocken/springjdbcandvalidation/domain](src/main/java/com/github/clocken/springjdbcandvalidation/domain)
    * Domain objects are annotated with lombok-annotation's and Java-Bean-Validation-annotation's
    * A custom Java-Bean-Validator to show "Foreign-Key-Validation" is located
      in [src/main/java/com/github/clocken/springjdbcandvalidation/domain/validation](src/main/java/com/github/clocken/springjdbcandvalidation/domain/validation)
* Repositories / DAO's are located
  in [src/main/java/com/github/clocken/springjdbcandvalidation/repository](src/main/java/com/github/clocken/springjdbcandvalidation/repository)
* REST controller is located
  in [src/main/java/com/github/clocken/springjdbcandvalidation/web/rest](src/main/java/com/github/clocken/springjdbcandvalidation/web/rest)

See code and its comments and the [Test's](src/test/java) for more information on the implementation.

Used technologies:

* [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
* [Spring JDBC](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc)
* [Spring Validation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation)
  / [Java Bean Validation](https://beanvalidation.org/)
* [Lombok](https://projectlombok.org/) to reduce boilerplate code
  in [Domain Object's](src/main/java/com/github/clocken/springjdbcandvalidation/domain)

Possible improvements:

* Use [Spring Data's repository abstraction](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories)
to auto-generate the [Repositories](src/main/java/com/github/clocken/springjdbcandvalidation/repository)
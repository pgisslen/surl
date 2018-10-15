# sURL

Features:

- API to create an shortcut to given URL. Provide the URL and an optional digest (defaults to CRC32)
- Use the shortcut to be redirected to the original URL.
- Documentet with Swagger. Swagger UI present at /swagger
- Database migration upon startup (Using liquibase) or by manual command.


How to start the application

---

1. Run `mvn clean install` to build your application. This runs the integration test
2. Start application with embedded DB: `java -jar target/surl-1.0-SNAPSHOT.jar server localhost_config.yml`
3. OR start application with stand alone DB: `java -jar target/surl-1.0-SNAPSHOT.jar server config.yml`
4. Service is documented using Swagger @ `http://localhost:8080/swagger` You can use it to try out the API as well

Required: Java 8 (untested with newer versions) and Maven.

---
To run with stand alone DB:

1. Update datasource config.yml match the running db instance
2. Optional: Run `java -jar target/surl-1.0-SNAPSHOT.jar db migrate config.yml` if not run, DB will be migrated during startup

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

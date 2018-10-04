# sURL

How to start the application

---

1. Run `mvn clean install` to build your application. This runs the integration test
2. Start application with embedded DB: `java -jar target/surl-1.0-SNAPSHOT.jar server localhost_config.yml`
3. OR start application with stand alone DB: `java -jar target/surl-1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter url `http://localhost:8080`

Required: Java 8 (untested with newer versions) and Maven.

---
To run with stand alone DB:

1. Update datasource config.yml match the running db instance
2. Run `java -jar target/surl-1.0-SNAPSHOT.jar db migrate config.yml`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

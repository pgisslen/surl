# sURL - s(hort)URL

How to start the application

---

1. Run `mvn clean install` to build your application. This runs the integration test
1. Start application with embedded DB: `java -jar target/surl-1.0-SNAPSHOT.jar server localhost_config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Required: Java 8 (untested with newer versions) and Maven.

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

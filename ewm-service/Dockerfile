FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]
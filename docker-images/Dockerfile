FROM maven
RUN mkdir /opt/customer-service
COPY target/microservice-customer-service.jar /opt/customer-service/app-build.jar
ENTRYPOINT ["java","-jar","/opt/customer-service/app-build.jar"]
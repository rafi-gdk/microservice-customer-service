FROM maven
RUN mkdir /opt/customer-service
COPY target/*-SNAPSHOT.jar /opt/customer-service/app-build.jar
ENTRYPOINT ["java","-jar","/opt/customer-service/app-build.jar"]
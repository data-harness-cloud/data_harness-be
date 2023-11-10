FROM reg.aidb.site/library/maven:v1 as builder
COPY . . 
RUN mvn clean 
RUN mvn install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

FROM reg.aidb.site/library/openjdk8-jre:v1
WORKDIR /app
COPY --from=builder application-webadmin/target/application-webadmin-1.0.0.jar /app/
CMD ["sh", "-c", "java -jar -Duser.timezone=Asia/Shanghai application-webadmin-1.0.0.jar"]
EXPOSE 8082
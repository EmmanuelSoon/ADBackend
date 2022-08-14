FROM maven
WORKDIR /app
COPY . /app
RUN mvn package -Dmaven.test.skip=true
CMD ["mvn","spring-boot:run"]
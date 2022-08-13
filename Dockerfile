FROM maven
WORKDIR /app
COPY . /app
RUN mvn package -DskipTests
CMD ["mvn","spring-boot:run"]
FROM openjdk:24-oraclelinux9
# Set working directory trong container
WORKDIR /app
COPY ./target/blog-0.0.1-SNAPSHOT.jar app.jar
# Copy the .env file
COPY .env /app/.env
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

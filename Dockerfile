FROM frolvlad/alpine-java:jdk8.202.08-slim
COPY target/product-2.0.jar /
CMD ["java", "-jar", "product-2.0.jar"]

FROM fedora:39
WORKDIR /app
COPY target/oda-widget-service /app

CMD ["./oda-widget-service"]

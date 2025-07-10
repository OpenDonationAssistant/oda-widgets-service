FROM fedora:41
WORKDIR /app
COPY target/oda-widget-service /app

CMD ["./oda-widget-service"]

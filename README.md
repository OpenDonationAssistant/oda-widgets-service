# ODA Widgets Service
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/OpenDonationAssistant/oda-widgets-service)
![Sonar Tech Debt](https://img.shields.io/sonar/tech_debt/OpenDonationAssistant_oda-widgets-service?server=https%3A%2F%2Fsonarcloud.io)
![Sonar Violations](https://img.shields.io/sonar/violations/OpenDonationAssistant_oda-widgets-service?server=https%3A%2F%2Fsonarcloud.io)
![Sonar Tests](https://img.shields.io/sonar/tests/OpenDonationAssistant_oda-widgets-service?server=https%3A%2F%2Fsonarcloud.io)
![Sonar Coverage](https://img.shields.io/sonar/coverage/OpenDonationAssistant_oda-widgets-service?server=https%3A%2F%2Fsonarcloud.io)

## Running with Docker

### Using GitHub Container Registry

Pull the latest image:
```bash
docker pull ghcr.io/opendonationassistant/oda-widgets-service:latest
```

Run the container with required environment variables:

```bash
docker run -d \
  --name oda-widgets-service \
  -p 8080:8080 \
  -e JDBC_URL=jdbc:postgresql://postgres:5432/widget \
  -e JDBC_USER=postgres \
  -e JDBC_PASSWORD=your_password \
  -e RABBITMQ_HOST=rabbitmq \
  -e INFINISPAN_HOST=infinispan \
  ghcr.io/opendonationassistant/oda-widgets-service:latest
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `JDBC_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost/postgres?currentSchema=widget` |
| `JDBC_USER` | Database username | `postgres` |
| `JDBC_PASSWORD` | Database password | `postgres` |
| `RABBITMQ_HOST` | RabbitMQ host | `localhost` |
| `INFINISPAN_HOST` | Infinispan host | `127.0.0.1` |

### Using Docker Compose

Example `docker-compose.yml`:

```yaml
services:
  widgets-service:
    image: ghcr.io/opendonationassistant/oda-widgets-service:latest
    ports:
      - "8080:8080"
    environment:
      - JDBC_URL=jdbc:postgresql://postgres:5432/widget
      - JDBC_USER=postgres
      - JDBC_PASSWORD=postgres
      - RABBITMQ_HOST=rabbitmq
      - INFINISPAN_HOST=infinispan
    depends_on:
      - postgres
      - rabbitmq
      - infinispan

  postgres:
    image: postgres:16
    environment:
      - POSTGRES_DB=widget
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data

  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "15672:15672"

  infinispan:
    image: infinispan/server:15.0
    ports:
      - "11222:11222"

volumes:
  postgres_data:
```

Run with:
```bash
docker-compose up -d
```

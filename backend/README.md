# Checkout Kata (Backend Service)

This project implements the backend service for a simplified supermarket checkout system. The service calculates the total price of items in a cart while automatically applying weekly promotional offers.

The system supports flexible pricing rules such as multi-buy discounts (e.g., "2 apples for €0.45") and percentage-based discounts. Offers are configured externally and loaded at application startup, allowing pricing rules to be modified without changing checkout logic.

The service exposes a REST API that accepts a cart payload and returns the computed checkout total. The API is documented using OpenAPI (Swagger) and can be run locally or via Docker.

## Key Features

- REST API for cart checkout
- Automatic application of active offers
- Configurable offers loaded from JSON at startup
- Clean domain modeling with value objects
- OpenAPI (Swagger) API documentation
- Docker support for running the backend service
- Unit and integration tests with JaCoCo coverage

## Architecture Overview

The backend follows a layered architecture that separates domain logic, pricing rules, application services, and infrastructure concerns. This structure keeps the checkout logic easy to test and extend while avoiding tight coupling between components.

The main layers are:

**Domain**

Core business concepts such as `Money`, `Product`, `Cart`, and `CartItem`.  
These classes represent the fundamental entities used during checkout and enforce important invariants (e.g., non-negative monetary values).

**Pricing**

Contains the pricing rules and offer implementations such as `MultiBuyOffer` and `PercentDiscountOffer`.  
Pricing logic is isolated from the rest of the system so that new offer types can be added without modifying checkout orchestration.

**Application**

Application services such as `CheckoutService` coordinate the checkout process by retrieving products, applying pricing rules, and producing the final result.

**Infrastructure**

Infrastructure components provide implementations for catalogs, offer loading, and configuration. For example:

- `InMemoryCatalog` stores product definitions
- `OfferLoader` loads offer configurations from JSON at application startup
- `OfferFactoryRegistry` creates offer instances from configuration

**API**

The REST controller exposes the checkout functionality via HTTP and returns the computed total.

A low-level design diagram illustrating these components will be included below.

## Low Level Design

![Architecture Diagram](docs/checkout-architecture.pdf)

## Project Structure

The project is organized into clear packages that separate domain logic, pricing rules, application services, infrastructure, and the API layer.

src/main/java/com/haiilo/checkout

api  
REST controllers and API request/response models

domain  
Core domain models and value objects  
(e.g., Money, Product, ProductId, Cart, CartItem)

pricing  
Pricing rules and offer implementations  
(e.g., MultiBuyOffer, PercentDiscountOffer, ValidityPeriod)

application  
Application services coordinating business workflows  
(e.g., CheckoutService)

infrastructure  
Infrastructure components such as catalogs, offer loading, and factories

exception  
Domain-specific exceptions used by the application
(e.g., UnknownProductException)

## Running the Application

### Prerequisites

- Java 21+ (or the version used by the project)
- Docker (optional, if running the containerized version)

---

### Run Locally (Gradle)

#### Start the application using Gradle:

```bash
./gradlew bootRun
```

The Service will start on: http://localhost:8080

### Run Using Docker

You can run the backend service inside a Docker container.

#### Build the Docker image

```bash
docker build -t checkout-backend .
```

#### Run the container

```bash
docker run -p 8080:8080 checkout-backend
```

### API Documentation (Swagger)

The REST API is documented using OpenAPI and can be explored interactively using Swagger UI.

Once the application is running, open:

http://localhost:8080/swagger-ui/index.html

Swagger UI allows you to:

- Explore available endpoints
- View request and response schemas
- Execute API requests directly from the browser

The OpenAPI specification is also available in JSON format:

http://localhost:8080/v3/api-docs

## Example API Request

The checkout endpoint accepts a cart payload and returns the calculated total after applying any applicable offers.

### Endpoint

POST /api/v1/checkout

### Request Example

{
\"items\": [
{
\"productId\": \"APPLE\",
\"quantity\": 3
}
]
}

### Response Example

{
\"total\": \"0.75\",
\"currency\": \"EUR\"
}

### Example using curl

curl -X POST http://localhost:8080/api/v1/checkout \
-H \"Content-Type: application/json\" \
-d '{
\"items\": [
{
\"productId\": \"APPLE\",
\"quantity\": 3
}
]
}'

### Example Error Response

If a product does not exist in the catalog:

{
\"error\": \"UNKNOWN_PRODUCT\",
\"message\": \"Unknown product: MANGO\"
}

## Offers Configuration

Pricing offers are configured externally and loaded at application startup. This allows pricing rules to be modified without changing the checkout logic.

Offers are defined in a JSON configuration file and converted into domain offer objects when the application starts.

Example configuration:

[
{
\"type\": \"MULTI_BUY\",
\"productId\": \"APPLE\",
\"validFrom\": \"2026-03-01\",
\"validUntil\": \"2026-03-31\",
\"requiredQuantity\": 2,
\"bundlePrice\": \"0.45\"
},
{
\"type\": \"PERCENT_DISCOUNT\",
\"productId\": \"BANANA\",
\"validFrom\": \"2026-03-01\",
\"validUntil\": \"2026-03-31\",
\"percentage\": 10
}
]

At startup, the application:

1. Loads the configuration file
2. Converts each configuration entry into an offer object
3. Stores the offers in memory for fast lookup during checkout

This design keeps pricing rules configurable while keeping the checkout service independent from specific offer implementations.

## Testing

The project includes both unit tests and integration tests to verify domain logic, pricing rules, and API behavior.

Unit tests cover the core domain and pricing components such as `Money`, `MultiBuyOffer`, `PercentDiscountOffer`, and `ValidityPeriod`.

Integration tests validate the REST API and the interaction between application components.

### Running tests

Run all tests using:

./gradlew clean check

### Test Coverage

Test coverage is generated using JaCoCo.

To generate the coverage report:
```bash
./gradlew clean check jacocoTestReport
```

The HTML report will be available at:

build/reports/jacoco/test/html/index.html

## Design Decisions

### Stateless Checkout

The checkout API is stateless. The full cart payload is provided in each request, and the service calculates the total price without storing cart state. This keeps the service simple and easy to scale.

### External Offer Configuration

Offers are defined in a JSON configuration file and loaded at application startup. This allows pricing rules to be modified without changing checkout logic.

### Domain Modeling

Key concepts such as `Money`, `Product`, `ProductId`, `Cart`, and `CartItem` are modeled as domain objects. This helps enforce business invariants (e.g., non-negative monetary values) and keeps pricing logic explicit.

### Isolated Pricing Rules

Pricing rules are implemented separately from checkout orchestration. Offer implementations such as `MultiBuyOffer` and `PercentDiscountOffer` encapsulate their own pricing behavior, making the system easier to extend with new offer types.

## Future Improvements

Several improvements could be made if this service were extended further:

### Persistent Catalog and Offers
Products and offers are currently loaded in memory. In a production system, these would likely be stored in a database or configuration service.

### Additional Offer Types
The pricing system can be extended to support additional offer types such as buy-one-get-one, cart-level discounts, or category-based promotions.

### Currency Support
The current implementation assumes EUR. Support for multiple currencies and exchange handling could be introduced if required.

### Observability
Metrics and structured logging could be added to improve monitoring and operational visibility.

### Containerization Enhancements
Docker images could be optimized further using multi-stage builds or minimal runtime images for improved efficiency.
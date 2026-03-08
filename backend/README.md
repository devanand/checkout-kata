# Checkout Kata (Backend Service)

This project implements the backend service for a simplified supermarket checkout system.

The service calculates the total price of items in a cart and automatically applies promotional offers such as multi-buy discounts and percentage discounts. Offers are stored in a database and assigned to products through a flexible mapping model.

The system is implemented using Java and Spring Boot with a focus on clear domain modeling, extensible pricing rules, and a clean separation between checkout orchestration and pricing logic.

The service exposes a REST API for checkout operations, documented with OpenAPI (Swagger), and can be run locally or via Docker.

## Key Features

• REST API for cart checkout  
• Automatic application of active promotional offers  
• Multi-buy and percentage discount pricing rules  
• Offers stored in a database and assigned to products  
• Clean domain model with extensible pricing logic  
• OpenAPI (Swagger) API documentation  
• Docker support for running the service  
• Unit and integration tests with JaCoCo coverage

## Architecture Overview

The service follows a layered architecture that separates checkout orchestration, pricing logic, and persistence concerns. This structure keeps the checkout flow easy to understand while allowing pricing rules to evolve independently.

The system is illustrated using three complementary diagrams.

### 1. Database Schema

The persistence model contains three main tables:

- `products` – product catalog entries with pricing information
- `offers` – definitions of promotional offers and their parameters
- `offer_assignments` – mapping between products and applicable offers

This design decouples offer definitions from product applicability and allows offers to be reused across multiple products.

### 2. Checkout Application Flow

This diagram shows how the checkout process is orchestrated.

The main components are:

- `CheckoutService` – coordinates the checkout process
- `Catalog` – retrieves product information
- `PricingService` – calculates the final price for each cart item
- `OfferCatalog` – resolves active offers for a product

During checkout:

1. The cart is processed item by item
2. Products are retrieved from the catalog
3. Pricing rules are applied via the pricing service
4. Results are aggregated into the final checkout response

### 3. Pricing and Offer Model

Pricing rules are implemented using a flexible domain model.

Key classes include:

- `Offer` – abstraction for pricing rules
- `AbstractOffer` – shared offer behavior and metadata
- `MultiBuyOffer` – bundle pricing (e.g., 2 items for a fixed price)
- `PercentDiscountOffer` – percentage-based discounts
- `OfferAssignment` – associates offers with specific products
- `ValidityPeriod` – controls when an offer is active

This design allows new offer types to be added without modifying checkout orchestration.

## Project Structure

The repository is organized as a monorepo.

checkout-kata/

## Project Structure

The repository is organized as a monorepo.

backend/<br>
├── src/main/java/com/haiilo/checkout<br>
│   ├── api            # REST controllers and request/response DTOs<br>
│   ├── domain         # Core domain objects (Money, Product, Cart)<br>
│   ├── pricing        # Pricing logic and offer implementations<br>
│   ├── catalog        # Product and offer catalogs<br>
│   ├── persistence    # JPA entities and repositories<br>
│   └── service        # Checkout orchestration services<br>
│<br>
├── src/test/java<br>
│   ├── unit           # Unit tests<br>
│   └── integration    # Integration tests<br>
│<br>
└── Dockerfile<br>

The backend module contains the Spring Boot service implementing the checkout API, pricing engine, persistence layer, and automated tests.

## Running the Application

### Run locally

Start the Spring Boot application:
~~~bash
./gradlew bootRun
~~~
The service will be available at:

http://localhost:8080


### Run with Docker

Build the image:
~~~bash
docker build -t checkout-kata .
~~~
Run the container:
~~~bash
docker run -p 8080:8080 checkout-kata
~~~

## API Documentation

The API is documented using OpenAPI (Swagger).

Once the application is running, the Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

For convenience, the root URL automatically redirects to Swagger:

http://localhost:8080

From the Swagger UI you can:

- Explore all available endpoints
- Inspect request and response models
- Execute API requests directly from the browser

## Testing and Coverage

The project includes both unit and integration tests.

Run the full test suite and generate the coverage report with:

~~~bash
./gradlew clean check jacocoTestReport
~~~
JaCoCo is used to measure test coverage.

![Coverage](https://img.shields.io/badge/coverage-89%25-brightgreen)

The generated coverage report can be found at:

build/reports/jacoco/test/html/index.html

## Design Decisions

Several design choices were made to keep the system simple while allowing it to evolve.

**Offer–Product Decoupling**

Offers are defined independently of products and linked through an `offer_assignments` table.  
This enables the same offer to be reused across multiple products and keeps pricing rules separate from catalog data.

**Extensible Pricing Model**

Pricing rules are implemented through the `Offer` abstraction with concrete implementations such as `MultiBuyOffer` and `PercentDiscountOffer`.  
New pricing strategies can be added without modifying checkout orchestration.

**Checkout Orchestration**

`CheckoutService` coordinates the checkout flow while delegating pricing logic to `PricingService`.  
This separation keeps business logic modular and easier to test.

**Database-Backed Configuration**

Offers are stored in the database rather than static configuration files.  
This allows promotional rules to be updated without code changes.

**Domain-Focused Types**

Value objects such as `Money`, `ProductId`, and `ValidityPeriod` encapsulate domain rules and prevent primitive overuse.

## Future Improvements

Possible extensions to the current implementation include:

• Extract the offer evaluation logic into a dedicated offer engine module that could evolve into an independent service  
• Support additional promotional strategies (bundle discounts, seasonal offers)  
• Administrative APIs for managing products and offers  
• Persistent carts and user sessions  
• Multi-currency pricing support  
• Observability improvements (metrics and tracing)
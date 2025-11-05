# Backend API Test Automation Framework — `test-task-for-spribe`

**Author:** [Vladyslav S.](doxiqo@proton.me)

**Purpose:** Demonstration of a professional backend API automation framework built in Java for a Senior AQA technical
assignment.

---

## Overview

This project represents a production-grade **REST API testing framework** designed with a clean architecture and
maintainability in mind.  
It demonstrates solid engineering principles (SOLID, DRY, SRP), use of common design patterns, parallel execution
support, and detailed logging/reporting.

### ✅ Key Features

- Full API testing framework based on **Java 21 + TestNG + RestAssured + Gradle**
- **SOLID architecture** and reusable components
- **Design Patterns:** Factory, Facade, Builder, Validator
- **Parallel test execution** via TestNG configuration
- **Environment management** via property files (local/test/dev/stage)
- **Rich logging** with unified console + file log output
- **Allure reporting** with result collection and environment configuration
- **Thread-safe RestAssured setup**

---

## Technologies

| Tool                    | Purpose                            |
|-------------------------|------------------------------------|
| **Java 21**             | Core language                      |
| **Gradle (Kotlin DSL)** | Build system                       |
| **TestNG**              | Test runner                        |
| **RestAssured**         | API client and assertions          |
| **Jackson**             | JSON serialization/deserialization |
| **Allure**              | Test reporting                     |
| **Logback + SLF4J**     | Structured logging                 |

---

## Project Structure

```
├─ build.gradle.kts
├─ src/
│  ├─ main/java/ua/com/vladyslav/spribe/
│  │   ├─ api/         # BaseApiClient, services, facades
│  │   ├─ config/      # Environment config, RestAssured setup
│  │   ├─ data/        # Builders and test data factories
│  │   ├─ logging/     # Log abstraction + RestAssuredLogFilter
│  │   ├─ listeners/   # TestNG + Allure integration
│  │   ├─ models/      # Request/Response DTOs
│  │   └─ validators/  # Response validation utilities
│  └─ test/java/...    # Test classes grouped by feature
└─ src/test/resources/
   ├─ config/{env}.properties
   └─ testng.xml
```

---

## How to Run

### Prerequisites

- JDK 21+
- (Optional) Allure CLI for local report visualization

### Run tests locally

```bash
git clone https://github.com/doxiqo/test-task-for-spribe.git
cd test-task-for-spribe
./gradlew clean test
```

By default, environment = `dev`.

`dev` will use test env that was provided in test task specification

To specify another environment:

```bash
./gradlew test -Denv=local
```

### View Allure Report

```bash
allure generate build/allure-results -o build/allure-report --clean
allure serve build/allure-results  
```

### Logs

Logs are saved under `<rootDirectory>/logs`:

- `test-run-<runId>.log` — one per test run (parallel-safe)

---

## Logging & Filters

Custom `RestAssuredLogFilter` formats requests/responses in a clean and readable way:

- **INFO**: request method, URI, response code, time
- **DEBUG**: headers and full body  
  Thread-safe and easily adjustable per log level.

---

## Test Architecture & Design

- **Factory Pattern:** centralized creation of API services
- **Facade Pattern:** simplifies complex operations for test layers
- **Builder Pattern:** flexible and readable test data setup
- **Validator Pattern:** reusable assertions for API responses

Each test is isolated, repeatable, and safe for parallel execution.

---

## Adding New Tests

1. Create a new class in `src/test/java/...`
2. Extend the base test class (`BaseTest` or feature-specific base)
3. Use `PlayerApiFacade` or similar API services to interact with endpoints
4. Build request objects using builders/factories
5. Group with TestNG annotations (`@Test(groups = {"positive"})`)

---

## Configuration Management

- Properties per environment located at `src/test/resources/config/`
- Controlled via `-Denv` system property
- Default = `dev` environment
- `ConfigurationManager` sets up RestAssured base URI and specs automatically

---

## Notes

- Each run produces an independent log file (parallel-safe).
- Thread isolation ensures consistent behavior in concurrent TestNG execution.

---

## License

This project is for demonstration and educational purposes only.  
Use or modify freely for review or portfolio purposes.

---

**Author:** [Vladyslav S.](doxiqo@proton.me)

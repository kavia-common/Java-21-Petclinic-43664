# my-bank Java 21 Migration Verification & Validation Plan (Java-21-Petclinic-43664)

## 1. Scope and Objectives
This document defines the end-to-end verification and validation plan for the migration of the my-bank REST API from Spring Boot 2.1.x on Java 8 to Spring Boot 3.x on Java 21. The migrated service is integrated into the Java-21-Petclinic-43664 repository and must preserve functional behavior (endpoints and contracts), ensure non-functional compliance (builds on Java 21, health endpoints, OpenAPI, logging), and confirm basic runtime performance. The plan covers environment setup, verification matrix, step-by-step procedures, acceptance criteria, rollback validation, and known limitations.

Objectives:
- Validate functional parity of the my-bank API after migration to Java 21 and Spring Boot 3.x.
- Confirm data model persistence with H2 and seeding via data.sql.
- Verify API contracts, error handling, and status codes.
- Confirm Swagger/OpenAPI and health endpoints are available.
- Validate build and runtime using Maven Wrapper and as a packaged JAR.
- Verify Java version/toolchain and dependency compatibility.
- Perform performance smoke checks and basic operational readiness.
- Identify any deltas from pre-migration behavior and propose follow-ups.

Out of scope:
- New features, extensive performance benchmarking, comprehensive security hardening, or test-suite migration to JUnit 5 (recommended separately).

## 2. Test Environment and Prerequisites
Environment:
- OS: Linux or macOS (Windows commands are analogous).
- Java: 21 required (java -version must report 21).
- Maven Wrapper available in repo (./mvnw).
- Network access for Maven dependencies.
- Default service port: 8080 (use -Dserver.port=3002 for alternate port per README).

Tools:
- curl (required)
- jq (optional for JSON pretty-print)
- httpie (optional; alternative to curl)
- A shell with basic UNIX utilities (bash/sh)

Pre-checks:
- Verify Java version:
  - java -version → should show 21
  - ./mvnw -v → Java home/version must be Java 21
- Validate Maven Wrapper executable (chmod +x mvnw on Unix/macOS).
- Ensure port 8080 (or selected port) is free.
- Confirm repository paths and files exist (pom.xml, application.properties, data.sql, controllers, OpenAPI config).

## 3. Verification Matrix Overview
The matrix lists all verification areas with scenarios, expected behaviors before and after migration, a place to record actual results, evidence, status, and notes. Populate Actual Result, Evidence, Status, and Notes during execution.

| Area | Scenario | Pre-migration Expected | Post-migration Expected | Actual Result | Evidence (command/URL/log) | Status (Pass/Fail) | Notes |
|---|---|---|---|---|---|---|---|
| Functional parity | GET /accounts/all | 200 OK, list of accounts | 200 OK, list of accounts |  | curl -s http://localhost:8080/accounts/all |  |  |
| Functional parity | GET /accounts/{id} (existing id) | 200 OK, Account JSON | 200 OK, Account JSON |  | curl -s http://localhost:8080/accounts/1 |  |  |
| Functional parity | GET /accounts/{id} (missing id) | 404 Not Found | 404 Not Found |  | curl -i http://localhost:8080/accounts/99999 |  |  |
| Functional parity | POST /accounts/new (valid) | 200 OK with created Account | 200 OK with created Account |  | curl -s -X POST -H "Content-Type: application/json" -d '{"name":"Paul Roberts","balance":500.0}' http://localhost:8080/accounts/new |  |  |
| Functional parity | POST /accounts/new (invalid, blank name) | 400 Bad Request | 400 Bad Request |  | curl -i -X POST -H "Content-Type: application/json" -d '{"name":"","balance":5}' http://localhost:8080/accounts/new |  |  |
| Functional parity | PUT /accounts/{id}?amount=100.0 (existing id) | 200 OK, balance incremented | 200 OK, balance incremented |  | curl -s -X PUT "http://localhost:8080/accounts/1?amount=100.0" |  |  |
| Functional parity | PUT /accounts/{id}?amount=-50.0 (existing id) | 200 OK, balance decremented | 200 OK, balance decremented |  | curl -s -X PUT "http://localhost:8080/accounts/1?amount=-50.0" |  |  |
| Functional parity (CRUD) | DELETE /accounts/{id} | 200 OK (endpoint exists) | Should exist and return 200/204 |  | curl -i -X DELETE http://localhost:8080/accounts/2 |  | Migrated code does not include DELETE; expected FAIL unless scope accepts removal. |
| Data model & persistence | H2 schema + data.sql seeding | 2 seeded rows inserted | 2 seeded rows inserted (names may differ) |  | curl -s http://localhost:8080/accounts/all |  | Pre: John Smith/Tony Stark; Post: John Doe/Jane Smith |
| API contracts | Account JSON schema | id(Long), name(String), balance(Number) | id(Long), name(String), balance(Number) |  | curl -s http://localhost:8080/accounts/1 |  |  |
| Error handling | 404 mapping via @ResponseStatus | 404 on not found | 404 on not found |  | curl -i http://localhost:8080/accounts/99999 |  |  |
| Swagger/OpenAPI | OpenAPI JSON | /v3/api-docs available | /v3/api-docs available |  | curl -s http://localhost:8080/v3/api-docs |  |  |
| Swagger/OpenAPI | Swagger UI | /swagger-ui.html | /swagger-ui.html (redirects to /swagger-ui/index.html) |  | http://localhost:8080/swagger-ui.html |  |  |
| Health checks | Actuator health | /actuator/health 200 OK | /actuator/health 200 OK |  | curl -s http://localhost:8080/actuator/health |  |  |
| Health checks | Custom /healthz | Not guaranteed | /healthz 200 OK |  | curl -s http://localhost:8080/healthz |  |  |
| Build & run | Build with wrapper | ./mvnw -DskipTests clean package succeeds | Same with Java 21 |  | ./mvnw -DskipTests clean package |  |  |
| Build & run | Run via wrapper | ./mvnw spring-boot:run | ./mvnw spring-boot:run (Java 21) |  | ./mvnw spring-boot:run |  |  |
| Build & run | Run via jar | java -jar target/mybank-1.0.0.jar | java -jar target/mybank-2.0.0.jar |  | java -jar target/mybank-2.0.0.jar |  |  |
| CORS | Preflight OPTIONS | Not configured by default | Not configured unless added; should return without ACAO |  | curl -i -X OPTIONS http://localhost:8080/accounts/all -H "Origin:https://example.com" -H "Access-Control-Request-Method: GET" |  | Add CorsConfig if needed |
| Server URL config | OpenAPI servers | Static server URL | Overridable via mybank.openapi.server-url or env |  | curl -s http://localhost:8080/v3/api-docs |  | Expect servers[0].url reflects override |
| Logging | Default levels | Spring Boot defaults | Spring Boot defaults; overridable |  | startup logs; --logging.level.root=DEBUG |  |  |
| Java/toolchain | Java version | 8/11 | 21 |  | java -version; ./mvnw -v |  |  |
| Dependency compatibility | Spring Boot & springdoc | Boot 2.1.x + springdoc 1.x | Boot 3.2.x + springdoc 2.x starter |  | build succeeds; BOM in pom.xml |  |  |
| Performance smoke | GET /accounts/all latency | < 300 ms local | < 300 ms local |  | curl -w "time_total:%{time_total}\n" -o /dev/null -s http://localhost:8080/accounts/all |  | Thresholds adjustable |

Notes:
- The migrated code preserves GET/POST/PUT endpoints. The DELETE endpoint present pre-migration is not implemented post-migration; mark as a parity gap unless explicitly de-scoped.
- data.sql contents differ in seeded names; both seed two rows.

## 4. Step-by-step Verification Procedures

### 4.1 Build and Java Toolchain
1) Verify Java 21 is selected:
```bash
java -version
./mvnw -v
```
Expected: Java version 21.x; Maven Wrapper reports Java home pointing to JDK 21.

2) Build the application (skip tests if none or outdated):
```bash
./mvnw -DskipTests clean package
```
Expected: Build success, jar produced (e.g., target/mybank-2.0.0.jar).

### 4.2 Run the Service
Option A – Maven plugin (default port 8080):
```bash
./mvnw spring-boot:run
```

Option B – Custom port 3002 (recommended for previews):
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=3002 -Dserver.address=0.0.0.0"
```

Option C – Packaged JAR:
```bash
java -jar target/mybank-2.0.0.jar --server.port=8080 --server.address=0.0.0.0
```

### 4.3 Health and Observability
- Actuator health:
```bash
curl -s http://localhost:8080/actuator/health | jq
```
Expected: {"status":"UP"} or similar JSON.

- Custom healthz:
```bash
curl -s http://localhost:8080/healthz | jq
```
Expected: JSON with status "OK", timestamp, service "mybank".

### 4.4 Swagger/OpenAPI
- OpenAPI JSON:
```bash
curl -s http://localhost:8080/v3/api-docs | jq '.info.version, .servers[0].url'
```
Expected: version "2.0.0"; server URL reflects http://localhost:8080 or override if set.

- Swagger UI:
Open http://localhost:8080/swagger-ui.html (redirects to /swagger-ui/index.html).

- Override server URL (choose one):
```bash
# JVM property
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dmybank.openapi.server-url=https://preview.example.com:443"

# Or env var (for jar example)
MYBANK_OPENAPI_SERVER_URL=https://preview.example.com:443 \
java -jar target/mybank-2.0.0.jar
```
Re-check /v3/api-docs → servers[0].url matches override.

### 4.5 Functional Endpoints
- List all accounts:
```bash
curl -s http://localhost:8080/accounts/all | jq
```
Expected: array with seeded accounts (post-migration seed: John Doe, Jane Smith).

- Get account by id:
```bash
curl -s http://localhost:8080/accounts/1 | jq
```
Expected: account with fields id, name, balance.

- Create account (valid payload):
```bash
curl -s -X POST http://localhost:8080/accounts/new \
  -H "Content-Type: application/json" \
  -d '{"name":"Paul Roberts","balance":500.0}' | jq
```
Expected: 200 OK and created account JSON including generated id.

- Create account (invalid: blank name):
```bash
curl -i -X POST http://localhost:8080/accounts/new \
  -H "Content-Type: application/json" \
  -d '{"name":"","balance":5}'
```
Expected: 400 Bad Request due to @NotBlank on name.

- Update balance (increment by +100.0):
```bash
curl -s -X PUT "http://localhost:8080/accounts/1?amount=100.0" | jq
```
Expected: 200 OK; balance increased.

- Update balance (decrement by -50.0):
```bash
curl -s -X PUT "http://localhost:8080/accounts/1?amount=-50.0" | jq
```
Expected: 200 OK; balance decreased.

- Not found scenario:
```bash
curl -i http://localhost:8080/accounts/999999
```
Expected: HTTP/1.1 404 Not Found.

- Delete (parity check):
```bash
curl -i -X DELETE http://localhost:8080/accounts/2
```
Expected (pre-migration): 200 OK. Actual (post-migration): Likely 404/405 since DELETE not implemented in migrated code. Record in matrix.

### 4.6 Data Model and Persistence (H2)
- Confirm seeding via data.sql:
```bash
curl -s http://localhost:8080/accounts/all | jq 'length'
```
Expected: 2 or more results, depending on prior creates.

- Optional H2 console (for local dev only):
Open http://localhost:8080/h2-console (enabled in application.properties).

### 4.7 CORS and Server URL
- CORS preflight check (no CORS configured by default):
```bash
curl -i -X OPTIONS "http://localhost:8080/accounts/all" \
  -H "Origin: https://example.com" \
  -H "Access-Control-Request-Method: GET"
```
Expected: Response without Access-Control-Allow-Origin header (i.e., no permissive CORS). Add a CorsConfig if needed for browser clients.

- OpenAPI server URL override validation:
See 4.4 to set and verify mybank.openapi.server-url or MYBANK_OPENAPI_SERVER_URL.

### 4.8 Logging and Log Levels
- Default run logs show Spring Boot startup. To increase verbosity:
```bash
# Maven run
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dlogging.level.root=DEBUG"

# JAR run
java -jar target/mybank-2.0.0.jar --logging.level.root=DEBUG
```
Expected: more detailed DEBUG logs.

### 4.9 Java Version and Dependency Compatibility
- Verify Java:
```bash
java -version
./mvnw -v
```
Expected: Java 21.

- Confirm build plugins/dependencies (from pom.xml):
  - maven-compiler-plugin with <release>21</release>
  - Spring Boot 3.2.x BOM
  - springdoc-openapi-starter-webmvc-ui 2.x
  - actuator, validation, web, data-jpa, h2
Successful build is a strong proxy for compatibility.

### 4.10 Performance Smoke Checks
- Simple latency measurement:
```bash
curl -w "time_total: %{time_total}\n" -o /dev/null -s http://localhost:8080/accounts/all
```
Expected: time_total under local threshold (e.g., < 0.300s on a typical dev machine). Record values in matrix.

## 5. Acceptance Criteria and Sign-off Checklist
Functional:
- GET /accounts/all returns 200 OK with list.
- GET /accounts/{id} returns 200 OK for existing and 404 for missing.
- POST /accounts/new returns 200 OK with created entity; 400 on invalid payload.
- PUT /accounts/{id}?amount= returns 200 OK; balance adjusted correctly.
- Data seeding via data.sql occurs (at least 2 initial rows).

Non-functional:
- Build succeeds with Java 21 using ./mvnw -DskipTests clean package.
- Service runs via ./mvnw spring-boot:run and via packaged JAR (java -jar target/mybank-2.0.0.jar).
- OpenAPI available at /v3/api-docs and Swagger UI at /swagger-ui.html.
- Health endpoints: /actuator/health and /healthz return 200 OK.
- Logging operates with adjustable log level.
- Performance smoke check meets threshold.

Parity note:
- DELETE /accounts/{id} existed pre-migration in my-bank but is not present post-migration. Confirm whether this is acceptable (scope change) or requires implementation before sign-off.

Sign-off:
- All criteria above meet Pass status in the matrix (except documented, approved deviations).
- Stakeholders approve deviations (e.g., DELETE endpoint gap) or gap is remediated.

## 6. Rollback Validation Steps
If rollback is required:
1) Restore or redeploy the pre-migration my-bank service (original repository or pre-migration container).
2) Re-run critical checks:
   - GET/POST/PUT/DELETE endpoints function per pre-migration behavior.
   - Health endpoints available if previously configured.
   - Swagger/OpenAPI (if enabled) accessible.
3) Record results and confirm service stability before re-trying the migration.

## 7. Known Limitations and Follow-ups
- DELETE Endpoint Parity: The migrated code omits DELETE /accounts/{id}. If CRUD parity is required, implement the DELETE endpoint to match pre-migration behavior and re-verify.
- CORS: No explicit CORS configuration is provided. Browser clients may require CORS headers; add a CorsConfig if needed and re-run the CORS matrix scenario.
- Test Suite Migration: Legacy tests (JUnit 4) were part of the original repository. Consider migrating to JUnit 5 and adding integration tests for all endpoints on Boot 3.
- Seed Data Differences: data.sql seeds different names post-migration; this is acceptable for verification purposes but note the change for any demo/documentation alignment.
- Status Codes: POST returns 200 OK rather than 201 Created; parity with pre-migration retained. Consider enhancement later if desired.

## 8. Quick-run Critical Checks Script (compact)
This snippet assumes the service is running at http://localhost:8080. It performs a minimal set of health and API checks and exits non-zero on failure.

```bash
#!/usr/bin/env bash
set -euo pipefail

BASE="${BASE_URL:-http://localhost:8080}"
fail() { echo "FAIL: $*" 1>&2; exit 1; }

echo "Checking actuator health..."
curl -sf "$BASE/actuator/health" | jq -e '.status=="UP"' >/dev/null || fail "/actuator/health not UP"

echo "Checking healthz..."
curl -sf "$BASE/healthz" | jq -e '.status=="OK"' >/dev/null || fail "/healthz not OK"

echo "Listing accounts..."
ACCTS="$(curl -sf "$BASE/accounts/all")" || fail "GET /accounts/all"
echo "$ACCTS" | jq . >/dev/null 2>&1 || echo "$ACCTS"

echo "Creating account..."
NEW="$(curl -sf -X POST "$BASE/accounts/new" -H "Content-Type: application/json" -d '{"name":"Verifier User","balance":123.45}')" || fail "POST /accounts/new"
ID="$(echo "$NEW" | jq -r '.id')" || fail "No id in create response"
[ "$ID" != "null" ] || fail "Invalid id in create response"

echo "Updating account balance (+10)..."
curl -sf -X PUT "$BASE/accounts/$ID?amount=10.0" >/dev/null || fail "PUT /accounts/{id}?amount"

echo "Fetching updated account..."
curl -sf "$BASE/accounts/$ID" | jq -e '.balance>0' >/dev/null || fail "Balance not > 0"

echo "OpenAPI docs check..."
curl -sf "$BASE/v3/api-docs" | jq -e '.openapi' >/dev/null || fail "No OpenAPI document"

echo "All quick checks passed."
```

## Appendix A: API Contracts and Examples

### Account Object
- Fields:
  - id: Long (generated)
  - name: String (required, not blank)
  - balance: number (double)

Example create payload:
```json
{
  "name": "Paul Roberts",
  "balance": 500.0
}
```

Example response:
```json
{
  "id": 3,
  "name": "Paul Roberts",
  "balance": 500.0
}
```

### Status Codes
- GET /accounts/all → 200 OK
- GET /accounts/{id} → 200 OK or 404 Not Found
- POST /accounts/new → 200 OK, 400 Bad Request on validation errors
- PUT /accounts/{id}?amount= → 200 OK, 404 Not Found if id missing
- DELETE /accounts/{id} → Pre-migration existed; not present post-migration (gap)

## Appendix B: Build and Run Commands
- Build:
```bash
./mvnw -DskipTests clean package
```
- Run default port:
```bash
./mvnw spring-boot:run
```
- Run custom port:
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=3002 -Dserver.address=0.0.0.0"
```
- Run jar:
```bash
java -jar target/mybank-2.0.0.jar --server.port=8080 --server.address=0.0.0.0
```
- OpenAPI server URL override:
```bash
# JVM property
-Dmybank.openapi.server-url=https://host:port

# Or environment variable
MYBANK_OPENAPI_SERVER_URL=https://host:port
```

## Appendix C: Evidence Pointers
- Source files (post-migration):
  - pom.xml (Java 21 toolchain, Spring Boot 3.x, springdoc-starter 2.x)
  - src/main/resources/application.properties (actuator health, swagger settings, H2 console)
  - src/main/resources/data.sql (seed rows)
  - com.marcoslombog.mybank.controller.AccountController (GET/POST/PUT endpoints)
  - com.marcoslombog.mybank.controller.HealthController (/healthz)
  - com.marcoslombog.mybank.config.OpenApiConfig (server URL override support)
- Source files (pre-migration):
  - AccountController (includes DELETE endpoint)
  - data.sql (seeds two rows)
  - pom.xml (Boot 2.1.x, Java 1.8, springdoc 1.x)

# Expense Tracker

A single-user expense tracker built with Java, Spring Boot, JPA, H2/MySQL, and vanilla HTML/CSS/JavaScript. It is intentionally structured as a learning project: the frontend calls REST endpoints, controllers delegate to services, and services calculate financial values from repository data.

## What it includes

- Transaction CRUD, pagination, filtering, date-range filtering, search, and sorting
- Dashboard totals, monthly income-vs-expense chart, and expense category chart
- Monthly budgets with on-track, 80% warning, and exceeded states
- Server-side validation, consistent JSON error responses, `BigDecimal` money handling
- Responsive frontend that uses Fetch and Chart.js; no browser storage or hardcoded financial totals

## Architecture

`static/app.js → REST controller → service → Spring Data JPA repository → database`

- **Controllers** translate HTTP requests into Java method calls and return HTTP responses.
- **Services** contain business rules: totals, filtering, reports, and budget progress. Spring injects dependencies through constructors.
- **Repositories** are interfaces that Spring Data JPA turns into database queries at runtime.
- **Entities** (`Transaction`, `Budget`) map Java objects to database tables. DTOs keep API input/output separate from those internal entities.
- **BigDecimal** represents currency exactly, avoiding the rounding errors of `double`.

## Project layout

```
src/main/java/com/example/expensetracker/
  controller/   REST API endpoints
  service/      financial calculations and business rules
  repository/   JPA database access
  model/        entities and transaction type enum
  dto/          API request/response records
  exception/    centralized API error handling
src/main/resources/static/  browser UI
src/main/resources/         H2 and MySQL configuration
```

## Run locally (fastest: H2)

Prerequisite: JDK 21+ and Maven 3.9+.

```bash
mvn spring-boot:run
```

Open http://localhost:8080. The H2 database is stored locally under `data/` (which Git ignores). Its development console is at http://localhost:8080/h2-console using URL `jdbc:h2:file:./data/expense-tracker`, username `sa`, and an empty password.

## Run with MySQL

Create a MySQL user with access to a database (the default URL can create `expense_tracker`), then set the environment variables in `.env.example` in your terminal and run:

```bash
export SPRING_PROFILES_ACTIVE=mysql
export DB_USERNAME=expense_user
export DB_PASSWORD=your-secret
mvn spring-boot:run
```

Never commit a real `.env` file; it is ignored by Git.

## Deploy the Vercel frontend with a Java backend

Vercel serves the static browser files but does **not** run this Spring Boot application. Deploy the Java backend to a Java/Docker host (for example Render), configure its MySQL and `CORS_ALLOWED_ORIGINS` environment variables, and set the frontend API address.

1. Create a MySQL database and deploy this repository to Render using `render.yaml`.
2. In Render, set `DB_HOST`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, and `CORS_ALLOWED_ORIGINS` to your Vercel domain (for example `https://expense-trackerr.vercel.app`).
3. In `src/main/resources/static/api-config.js`, set `window.EXPENSE_API_BASE_URL` to the public Render service URL, for example `https://expense-tracker-api.onrender.com`.
4. Deploy the frontend to Vercel again. The Add Transaction form will then call the live API rather than the missing `/api` endpoint.

## API quick reference

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/transactions` | Create transaction |
| GET | `/api/transactions?type=EXPENSE&category=Food&startDate=2026-11-01&endDate=2026-11-30&search=groceries` | List/filter transactions |
| GET/PUT/DELETE | `/api/transactions/{id}` | Read, update, delete one transaction |
| GET | `/api/dashboard/summary` | Income, expenses, balance, current month |
| GET | `/api/reports/monthly?year=2026` | 12 monthly summaries |
| GET | `/api/reports/categories` | Expense totals by category |
| POST | `/api/budgets` | Configure a monthly budget |
| GET | `/api/budgets/current` | Current month spending status |
| PUT | `/api/budgets/{id}` | Update a budget |

Example transaction request:

```json
{"type":"EXPENSE","amount":450.00,"category":"Food","description":"Weekly groceries","transactionDate":"2026-11-01"}
```

## Test and package

```bash
mvn test
mvn package
java -jar target/expense-tracker-0.0.1-SNAPSHOT.jar
```

## Push to GitHub

Create an empty repository on GitHub, then run these commands in this folder (replace the URL):

```bash
git init
git add .
git commit -m "Build expense tracker"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```

## Current scope and next steps

This is a local single-user application; it has no login or account isolation. Production work should add authentication, user ownership on entities, authorization, migrations (Flyway), dedicated database backups, and more API tests.

# Cinesphere Backend

Spring Boot 3.3 + Java 21 backend for the Cinesphere movie-ticket booking application.

## Stack

- Java 21
- Spring Boot 3.3.0
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security + JWT
- MySQL
- Jakarta Validation
- Springdoc OpenAPI / Swagger

## Architecture

```
com.tramell.cinesphere
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── mapper
├── repository
├── security
├── service
│   └── impl
└── util
```

The main request flow is:

`Controller -> Service -> Repository`

Entities are not exposed directly through API responses; response DTOs and mappers are used.

## API base path

All application APIs use:

`http://localhost:8080/tramell/cinesphere`

Swagger:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

## Main endpoints

### Public
- `POST /auth/register`
- `POST /auth/login`
- `GET /movies`
- `GET /movies/{id}`
- `GET /movies/showing`
- `GET /movies/search?keyword=...`
- `GET /theatres`
- `GET /theatres/{id}`
- `GET /shows/{id}`
- `GET /shows/movie/{movieId}`
- `GET /shows/theatre/{theatreId}`
- `GET /shows/{showId}/seats`
- `GET /coupons`
- `GET /coupons/validate/{code}`

### Authenticated
- `GET /users/me`
- `POST /bookings/user/{userId}`
- `GET /bookings/user/{userId}`
- `GET /payments/booking/{bookingId}`
- `GET /receipt/{bookingId}`

A customer can access only their own bookings/payments/receipts. Admins can access them where appropriate.

### Admin
- `POST /movies`
- `PUT /movies/{id}`
- `DELETE /movies/{id}`
- `POST /theatres`
- `POST /shows`
- `POST /shows/{showId}/seats`
- `POST /coupons`
- `PUT /payments/{paymentId}/status`
- `GET /admin/dashboard`

## Demo accounts

Demo account seeding is enabled by default for local development.

Default local credentials:

- Admin: `catherine@tramell.com` / `password`
- Customer: `nick@jensen.com` / `password`

Override them with environment variables or disable demo seeding:

```properties
app.seed-demo-users=false
```

For anything beyond local development, use strong credentials and disable demo seeding.

## Database

Configure MySQL in `src/main/resources/application.properties`.

The project uses:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
```

`data.sql` contains schema-compatible sample movies, theatre, shows, coupon, and seats.

### Important if an older Cinesphere database exists

The supplied project previously contained a different/legacy `data.sql` schema (for example `screens`, `location`, `total_screens`, `start_time`, and `price`). Those columns do not belong to the current JPA model.

If an old database has already been created from that schema, use a fresh database or migrate/drop the old Cinesphere tables before first running this corrected version.

## Booking consistency

Seat booking uses database row locking (`PESSIMISTIC_WRITE`) and a unique `(show_id, seat_number)` constraint.

The booking transaction:

1. validates the request
2. locks requested seats
3. validates the coupon
4. calculates the final amount
5. creates the booking
6. creates the simulated successful payment
7. marks seats as booked and associates them with the booking

A failure rolls the transaction back instead of silently returning a cancelled booking with partially committed data.

## Build

```bash
mvn clean verify
```

Run:

```bash
mvn spring-boot:run
```

Or run `CinesphereApplication` directly from STS.

## Frontend compatibility

The backend is aligned with the Cinesphere React frontend API base:

`http://localhost:8080/tramell/cinesphere`

It specifically provides `/users/me`, which is required by the frontend authentication and booking flow.


## Deep audit

See `DEEP_AUDIT.md` for the latest full re-audit, fixed defects, and remaining integration/domain considerations.

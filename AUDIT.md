# Cinesphere Backend Audit

## Verification performed

- Inspected all Java source files in the uploaded backend.
- Inspected Maven configuration and application configuration.
- Inspected database seed SQL.
- Inspected all controllers, services, repositories, entities, DTOs, mappers, security, exception handling, and project metadata.
- Compared API contracts against the supplied Cinesphere React frontend.
- Ran a source-level `javac -proc:none` syntax pass. Dependency-resolution errors are expected because the execution environment does not contain the Maven dependency classpath.
- The uploaded Maven log was also inspected; its recorded build failure was caused by Lombok annotation processing against JDK 25 (`NoSuchFieldException: com.sun.tools.javac.code.TypeTag :: UNKNOWN`).

## Important defects found and corrected

### Build/tooling
- Removed the unnecessary Lombok/JDK-sensitive annotation-processing setup.
- Removed unused Spring Cloud OpenFeign dependency.
- Removed unused ModelMapper dependency.
- Standardized the Maven project on Java 21, matching the Eclipse project metadata and the project's current runtime direction.
- Removed stale Maven error/debug artifacts and unrelated helper scripts.

### Database/schema
- Replaced the legacy `data.sql` schema assumptions with SQL matching the current JPA entities.
- Added `spring.jpa.defer-datasource-initialization=true`.
- Added compatible sample movies, theatre, shows, coupon, and seats.
- Added a unique `(show_id, seat_number)` constraint.
- Made `Coupon.isActive` non-null.
- Externalized the database password through `CINESPHERE_DB_PASSWORD`.

### Authentication
- Added the missing `GET /tramell/cinesphere/users/me` endpoint required by the React frontend.
- Added validation to login and registration requests.
- Normalized email addresses to lowercase.
- Replaced generic registration runtime errors with proper bad-request responses.
- Added proper invalid-credentials handling.
- Hardened JWT parsing and invalid-token handling.
- Externalized the JWT secret through `CINESPHERE_JWT_SECRET`.
- Added JSON 401/403 responses.

### Authorization
The original security configuration accidentally made entire movie and theatre paths public, including write operations.

Corrected:
- Movie create/update/delete -> ADMIN
- Theatre creation -> ADMIN
- Show creation -> ADMIN
- Seat generation -> ADMIN
- Coupon creation -> ADMIN
- Payment status updates -> ADMIN
- Booking history/creation -> only the owning customer or ADMIN
- Payment lookup -> only the booking owner or ADMIN
- Receipt lookup -> only the booking owner or ADMIN

### Booking integrity
The original booking flow had a major data-integrity problem: seats were marked BOOKED but were never associated with the Booking entity.

Corrected:
- Booked seats are now associated with their booking.
- Booking history can therefore return actual booked seats.
- Receipt generation can return actual booked seats.
- Duplicate seats in one request are rejected.
- Maximum booking size is enforced at 10 seats.
- Seat numbers are validated.
- Past shows cannot be booked.
- Booking uses a transaction.
- Seat lookup for booking uses pessimistic row locking.
- Seat generation is idempotent instead of creating duplicates.
- Booking failures roll back instead of silently returning a partially completed booking.

### Coupon logic
The original validation checked `isActive` but did not check `expiryDate`.

Corrected:
- Expired coupons are rejected.
- Coupon codes are normalized.
- Active coupon listings exclude expired coupons.
- Discount arithmetic uses explicit decimal rounding.

### Dashboard
The original dashboard counted all shows/bookings and summed all booking amounts.

Corrected:
- Active shows = future shows.
- Total bookings = confirmed bookings.
- Revenue = confirmed booking revenue.

### API/transaction behavior
- Added read-only transactions where DTO mapping accesses lazy JPA relationships.
- Improved exception handling for validation, conflicts, invalid credentials, and bad requests.
- Kept entity objects out of API responses through DTOs/mappers.

## Known environment requirement

The execution environment used for this audit does not have Maven installed, so a full `mvn clean verify` could not be executed here.

The uploaded project's own Maven log was available and showed the previous failure was during Lombok annotation processing under JDK 25. The corrected project removes that failure source.

Before using the corrected backend against an old local database, use a fresh Cinesphere database or migrate the old schema. The original `data.sql` belonged to a different/legacy schema containing fields such as `screens`, `location`, `total_screens`, `start_time`, and `price`, which do not exist in the current JPA model.

## Local demo accounts

Demo seeding is enabled for local development:

- Admin: `catherine@tramell.com`
- Customer: `nick@jensen.com`
- Default demo password: `password`

Disable demo seeding for production with:

```properties
app.seed-demo-users=false
```

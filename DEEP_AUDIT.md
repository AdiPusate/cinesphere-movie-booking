# Cinesphere Backend — Deep Re-Audit

Audit date: 2026-08-09

This pass re-reviewed the complete backend source tree, Maven configuration, application properties, seed SQL, entities/relationships, repositories, controllers, DTOs, mappers, services, security, exception handling, and frontend API contract.

## Findings from the re-audit

### Fixed in this build

1. **Coupon expiry bypass**
   - `validateCoupon()` previously checked `isActive` but not `expiryDate`.
   - It now rejects expired coupons and normalizes the code to uppercase.
   - Null `isActive` values on creation now default to active.

2. **Receipt lazy-loading failure**
   - `spring.jpa.open-in-view=false` is enabled.
   - Receipt generation accessed lazy `Booking -> Show -> Movie/Theatre` and `Booking -> ShowSeat` relationships outside an explicit transaction.
   - `ReceiptServiceImpl.generateReceiptHtml()` is now `@Transactional(readOnly = true)`.

3. **JWT for deleted/unknown users could become a server error**
   - The JWT filter now treats `AuthenticationException` (including `UsernameNotFoundException`) as an invalid authentication context and continues unauthenticated, allowing protected endpoints to return 401 instead of leaking a 500.

4. **API timestamps were null**
   - `ApiResponse` now initializes the timestamp when the builder does not supply one.

5. **Overlapping shows in one theatre**
   - Show creation now rejects time intervals that overlap an existing show in the same theatre, using the movie durations.

6. **Destructive movie deletion**
   - `Movie -> Show -> Booking/Seat/Payment` uses cascading relationships.
   - Deleting a movie that has shows could therefore destroy booking/payment history.
   - Movie deletion now refuses when shows exist and tells the admin to archive the movie instead.

7. **Malformed request/type errors**
   - Added 400 responses for malformed JSON and path/query parameter type mismatches instead of falling through to generic 500 handling.

8. **Registration normalization**
   - Registration now trims the user's name and uses `Locale.ROOT` for email normalization.

## Important issues intentionally NOT hidden

### Receipt URL vs browser navigation
The receipt endpoint is correctly protected by JWT authorization. A plain browser navigation to the returned `receiptUrl` does not automatically attach the `Authorization: Bearer ...` header stored in frontend localStorage.

Therefore a frontend implementation that uses `<a href="receiptUrl">` or `window.location.href = receiptUrl` will receive 401. The frontend must fetch the receipt with Axios (which adds the JWT) and then display/open the returned HTML, or the backend must introduce a separate short-lived signed receipt token. The current secure backend behavior should NOT be weakened by making receipts public by booking ID.

### Payment status semantics
The admin endpoint can change a payment to `FAILED` or `REFUNDED`, while the booking can remain `CONFIRMED`. This is a domain-model issue rather than an authentication bug. A production implementation should define explicit booking/payment state transitions and release/refund seats when appropriate.

### Show seat generation
Seat generation is idempotent for existing `(show, seatNumber)` pairs, but it is an admin action separate from show creation. The seeded shows already contain seats. A production workflow may choose to generate seats automatically when a show is created.

### Security configuration warning
Spring logs a warning because an explicit `AuthenticationProvider` bean is configured alongside a `UserDetailsService`. The provider itself explicitly uses that `UserDetailsService`, so the warning does not indicate a failed login path. It can be cleaned up for a quieter configuration later.

### Configuration for production
- `spring.jpa.hibernate.ddl-auto=update` is suitable for local/demo development but should be replaced with migrations for production.
- Demo credentials are enabled by default for local development and should be disabled in production.
- Default fallback secrets/passwords in `application.properties` are development conveniences and must be overridden in any deployed environment.
- CORS currently permits localhost:3000 and localhost:5173, matching the intended local frontend setup.

## Verification status

The supplied local runtime log proves that the backend starts successfully under Java 25, connects to MySQL, initializes JPA, installs the security filter chain, and starts Tomcat on port 8080.

A full Maven test/build cannot be executed in this analysis environment because Maven is not installed here. A `javac -proc:none` source pass was used only as a syntax sanity check; dependency-resolution errors are expected without the Maven classpath.

The backend should still be run locally with:

```bash
mvn clean verify
mvn spring-boot:run
```

and then tested end-to-end with the frontend.

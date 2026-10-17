# Acceptance Criteria: US-01 Add a Student

Each criterion is written as Given / When / Then and is covered by an automated test in
`StudentControllerTest` or `StudentServiceTest`.

| # | Scenario | Given / When / Then | Test |
|---|---|---|---|
| AC-1 | Valid input | Given no students exist, when POST `{"firstName":"Asha","lastName":"Patil","email":"asha@example.com","course":"IT"}`, then 201, a `Location` header, and a body containing a generated id and the same data | `createValidStudentReturns201` |
| AC-2 | Missing fields | When POST `{}`, then 400, message "Validation failed", and `fieldErrors` for firstName, lastName, email and course | `createWithMissingFieldsReturns400AndFieldErrors` |
| AC-3 | Blank field | When firstName is only spaces, then 400 with "First name is required" | `createWithBlankFirstNameReturns400` |
| AC-4 | Invalid email | When email is `not-an-email`, then 400 with "Email must be a valid address" | `createWithInvalidEmailReturns400` |
| AC-5 | Malformed JSON | When the body is not valid JSON, then 400 | `createWithMalformedJsonReturns400` |
| AC-6 | Duplicate email | Given a student with `asha@example.com` exists, when POST again with the same email (any capitalisation), then 409 and nothing new is stored | `createWithDuplicateEmailReturns409`, `createRejectsDuplicateEmail` |
| AC-7 | Creation and retrieval | Given a student was created, when GET `/api/students/{id}` with the returned id, then 200 and the same data | `getByIdReturnsStudent` |

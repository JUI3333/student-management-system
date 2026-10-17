# Architecture

Think of a restaurant: the **waiter** (controller) takes the order, the **chef** (service) applies the recipe and
rules, the **pantry** (repository) stores ingredients.

```
HTTP request
   │
   ▼
StudentController     receives JSON, @Valid checks it, picks the status code
   │
   ▼
StudentService        business rules: unique email, "not found", update logic
   │
   ▼
StudentRepository     Spring Data JPA, talks to H2 (in-memory database)
   │
   ▼
Student (entity)      the "students" table
```

| Package | Class | Role |
|---|---|---|
| `controller` | `StudentController` | REST endpoints under `/api/students` |
| `service` | `StudentService` | Business rules |
| `repository` | `StudentRepository` | Database access (SQL generated from method names) |
| `model` | `Student` | Database entity |
| `dto` | `StudentRequest`, `StudentResponse`, `ErrorResponse` | JSON shapes in/out; validation rules |
| `exception` | `GlobalExceptionHandler`, `StudentNotFoundException`, `DuplicateEmailException` | Turn errors into 400/404/409 JSON |
| root | `DemoDataLoader` | Sample data, only with the `demo` profile |

Design choices (kept simple on purpose):
- DTOs keep the database entity out of the API.
- Emails are trimmed and lower-cased, so `A@x.com` equals `a@x.com`.
- The database unique constraint backs up the service check.
- H2 is in-memory: no install, but data is lost on restart.
- Tests: `StudentServiceTest` (Mockito, no Spring) and `StudentControllerTest` (full Spring + H2 through MockMvc).

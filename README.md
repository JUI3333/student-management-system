# Student Management System with Automated CI/CD

A small Spring Boot REST API for managing student records, built for Agile Software Development FA2.
Every push to GitHub is automatically **built, tested, quality-checked and published** by GitHub Actions.

> **Honest status note:** the code, tests and workflow were written and statically checked
> (Checkstyle run, YAML and shell syntax validated). The Maven build, the tests and the GitHub Actions run
> must still be executed by you; see [Verification checklist](#verification-checklist).

## Technology stack

| Purpose | Tool |
|---|---|
| Language / IDE | Java 21, IntelliJ IDEA |
| Framework | Spring Boot 3.3.5 (Web, Data JPA, Validation) |
| Database | H2 in-memory (nothing to install) |
| Build | Apache Maven |
| Tests | JUnit 5, Mockito, MockMvc |
| Code quality | Checkstyle 10.17.0 via maven-checkstyle-plugin 3.4.0 |
| CI/CD | GitHub Actions |

## Prerequisites

- JDK 21 (`java -version` must show 21)
- Git, a GitHub account
- Maven 3.9+ (`mvn -v`). IntelliJ ships a bundled Maven, so installing it separately is optional for the IDE,
  but the terminal commands below need `mvn` on your PATH.

## Open in IntelliJ IDEA

1. **File > Open** and select the project folder (the one containing `pom.xml`). Choose *Open as Project*.
2. Wait for the Maven import (bottom-right progress bar).
3. **File > Project Structure > Project**: set SDK to **21**.
4. Open `StudentManagementApplication.java` and click the green arrow next to `main` to run it.

## Run the application

```bash
mvn spring-boot:run
# with two sample students pre-loaded:
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

The API is at `http://localhost:8080/api/students`. Data is lost on restart (in-memory H2).

## Run tests and quality checks

```bash
mvn test                  # JUnit 5 unit + integration tests
mvn checkstyle:check      # code quality (fails on violations)
mvn verify                # everything: compile, test, package, Checkstyle
mvn clean package         # builds target/student-management-system.jar
java -jar target/student-management-system.jar
```

## API endpoints

| Method | URL | Success | Errors |
|---|---|---|---|
| POST | `/api/students` | 201 Created | 400 invalid, 409 duplicate email |
| GET | `/api/students` | 200 OK | none |
| GET | `/api/students/{id}` | 200 OK | 404 not found, 400 bad id |
| PUT | `/api/students/{id}` | 200 OK | 400, 404, 409 |
| DELETE | `/api/students/{id}` | 204 No Content | 404 |

On **Windows PowerShell** use `curl.exe` (plain `curl` is an alias for something else), or use Postman, or the
IntelliJ built-in HTTP client.

### Sample requests and responses

```bash
# Create (201)
curl -i -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Asha","lastName":"Patil","email":"asha@example.com","course":"Information Technology"}'
```
```json
{"id":1,"firstName":"Asha","lastName":"Patil","email":"asha@example.com","course":"Information Technology"}
```
```bash
curl http://localhost:8080/api/students          # list (200)
curl http://localhost:8080/api/students/1        # one student (200)
curl -X PUT http://localhost:8080/api/students/1 -H "Content-Type: application/json" \
  -d '{"firstName":"Asha","lastName":"Deshmukh","email":"asha@example.com","course":"Computer Engineering"}'
curl -i -X DELETE http://localhost:8080/api/students/1     # 204
```

Validation error (400) when sending `{"email":"bad"}`:
```json
{"timestamp":"...","status":400,"error":"Bad Request","message":"Validation failed",
 "fieldErrors":{"firstName":"First name is required","lastName":"Last name is required",
                "email":"Email must be a valid address","course":"Course is required"}}
```
Duplicate email (409): `{"status":409,"error":"Conflict","message":"A student with email asha@example.com already exists",...}`
Unknown id (404): `{"status":404,"error":"Not Found","message":"Student with id 99 was not found",...}`

## Git and GitHub steps

```bash
git init
git add .
git commit -m "Initial commit: Student Management System with CI/CD"
git branch -M main
git remote add origin https://github.com/<your-username>/student-management-system.git
git push -u origin main
```
Create the empty repository on GitHub first (no README/.gitignore, since we already have them).
Add your teammate under **Settings > Collaborators**. Each member should commit their own work.

## CI/CD workflow (`.github/workflows/ci-cd.yml`)

Runs on push to `main`, on pull requests to `main`, and manually (**Actions > CI/CD Pipeline > Run workflow**).

```
build  ──►  test     ──┐
       └►  quality  ──┴──►  deploy (publish + smoke test, main branch only)
```

| Job | What it does |
|---|---|
| 1. Build | Java 21 + Maven cache, `mvn clean package -DskipTests`, uploads the JAR |
| 2. Automated Tests | `mvn test`, fails if any test fails, uploads surefire reports |
| 3. Code Quality | `mvn checkstyle:check`, fails on any violation, uploads the report |
| 4. Publish + Smoke Test | Downloads the verified JAR, **starts it on the runner**, calls the API with curl (expects 201, 409, list), then publishes the JAR artifact |

Details: [docs/ci-cd-explanation.md](docs/ci-cd-explanation.md).

### View runs and download the artifact

1. Repository > **Actions** tab > click the latest run.
2. The graph shows the four jobs; click one to see its logs.
3. Scroll to **Artifacts** at the bottom of the run summary and download `student-management-system-release`
   (the JAR) or `test-reports` / `checkstyle-report`.

### Deployment: what this is and is not

This project **publishes a verified, runnable JAR as a GitHub Actions artifact** and proves it starts using a
smoke test on the CI runner. It is **not** deployed to a permanently running public server.
Uploading an artifact is not the same as hosting. Hosting would need an external platform
(for example Render or Azure) plus credentials stored as **GitHub repository secrets**; none are used here.
Nothing in the repository contains secrets.

## Demonstrate a failing pipeline (good for the video)

- Add `import java.util.Map;` (unused) to `StudentService.java` and push: the **Code Quality** job fails.
- Change an expected value in a test (for example `isCreated()` to `isOk()`) and push: the **Tests** job fails.
- Revert, push again, and the pipeline turns green.

## Troubleshooting

| Problem | Fix |
|---|---|
| `release version 21 not supported` / wrong Java | Set JDK 21 in IntelliJ (Project Structure) and `JAVA_HOME` for the terminal |
| `mvn` is not recognized | Install Maven and add its `bin` folder to PATH, or use IntelliJ's Maven tool window |
| `Port 8080 was already in use` | Stop the other app or run with `--server.port=8081` |
| Checkstyle fails | Read the `[ERROR] file:line` message; remove unused imports, wrap lines over 120 chars |
| Workflow does not start | The file must be exactly `.github/workflows/ci-cd.yml` and pushed to `main` |
| `git push` rejected | Run `git pull --rebase origin main`, then push again |
| Artifact upload cannot find file | The JAR name is fixed by `<finalName>` in `pom.xml`; keep it unchanged |
| 415 Unsupported Media Type | Add the header `Content-Type: application/json` |

## Verification checklist

Verified in the authoring environment:
- [x] `checkstyle.xml` runs with Checkstyle 10.17.0 over all main and test sources with **0 violations**
- [x] Checkstyle **fails** when an unused import is added (negative test)
- [x] Workflow YAML parses; `scripts/smoke-test.sh` passes `bash -n`

**Not verified here (you must do these):**
- [ ] `mvn clean verify` succeeds on your machine (the authoring environment could not reach Maven Central)
- [ ] All tests pass (`mvn test`)
- [ ] App starts and the sample curl commands behave as documented
- [ ] GitHub Actions run is green on `main`; artifact downloadable
- [ ] Failing-pipeline demo behaves as described

# CI/CD Pipeline Explained

File: `.github/workflows/ci-cd.yml`. Triggers: push to `main`, pull request to `main`, manual run.

```
GitHub ► Build ► Automated Tests ► Code Quality ► Publish + Smoke Test
                  (parallel with quality)
```

| Stage | Job | Command | Why it matters | Fails when |
|---|---|---|---|---|
| Checkout | each job | `actions/checkout@v4` | Gets your code onto the clean runner | repo unreachable |
| Environment | each job | `actions/setup-java@v4` (Temurin 21, `cache: maven`) | Same Java as local; cache makes runs faster | n/a |
| Build | `build` | `mvn clean package -DskipTests` | Proves the code compiles and packs an executable JAR | compile error, bad pom |
| Test | `test` | `mvn test` | JUnit 5 checks behaviour: status codes, validation, duplicates | any test fails |
| Quality | `quality` | `mvn checkstyle:check` | Enforces consistent, clean code (unused imports, naming, line length) | any violation |
| Publish | `deploy` | `scripts/smoke-test.sh` then upload artifact | Starts the real JAR, calls the API, then publishes it | smoke test fails, JAR missing |

Key design points:
- `deploy` has `needs: [test, quality]`, so **unverified code is never published**.
- `deploy` runs only on `main` (never on pull requests).
- `-DskipTests` appears only in the build job because the next job runs the tests; tests are never skipped overall.
- No `continue-on-error` anywhere, so failures are visible.
- `permissions: contents: read` gives the workflow the minimum rights. No secrets are used or needed.
- Reports (`test-reports`, `checkstyle-report`) upload with `if: always()` so you can read them after a failure.

## Is this CI, Delivery or Deployment?
CI: yes. Delivery: the JAR is verified and ready to release. Deployment to a live server: **no**.
Uploading an artifact is not running an application on a server; the smoke test only starts the JAR temporarily on
the CI runner.

## Common failures
| Symptom | Cause | Fix |
|---|---|---|
| Red "Build" | compile error or wrong Java | read the log line with `[ERROR]`, fix, push |
| Red "Automated Tests" | a test assertion fails | open `test-reports` artifact or the log |
| Red "Code Quality" | style violation | log shows `file:line: message [RuleName]` |
| Deploy skipped | PR or non-main branch | expected; push to `main` |
| Smoke test timeout | app failed to start | read `app.log` output in the step |

## Not verified yet
This workflow has not been run on GitHub by the author of this file. Push the repo and confirm all four jobs are green.

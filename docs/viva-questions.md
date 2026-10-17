# Viva Questions and Answers

**1. What is Agile?** A way of building software in short cycles (sprints), delivering working increments, and adapting to feedback instead of planning everything upfront.

**2. What is a user story?** A feature described from the user's view: "As a [user], I want [feature], so that [benefit]." Ours include "Add a student".

**3. What are acceptance criteria?** Testable conditions that must be true for a story to be accepted, such as "duplicate email returns 409".

**4. What is the Definition of Done?** A shared checklist for every story (code pushed, build green, tests pass, Checkstyle passes, docs updated, artifact published). It applies to all stories; acceptance criteria apply to one.

**5. What happens in sprint planning?** The team picks stories for the sprint, sets a goal, breaks stories into tasks and assigns them.

**6. Difference between CI, Continuous Delivery and Continuous Deployment?** CI: automatically build and test every push. Delivery: every passing build is ready to release, with a manual release step. Deployment: every passing build is released automatically. Ours is CI plus artifact publishing.

**7. What is Maven and what is `pom.xml`?** A build tool that downloads dependencies and compiles, tests and packages the project. `pom.xml` lists dependencies, plugins and the Java version.

**8. Why use Spring Boot?** It auto-configures a web server, JSON, validation and database access so we write only our own logic.

**9. What is `mvn clean package`?** Deletes `target/`, compiles, runs tests, and builds the JAR. In the build job we add `-DskipTests` because the test job runs them separately.

**10. What is Git vs GitHub?** Git is the version-control tool on your computer. GitHub hosts repositories online and runs GitHub Actions.

**11. What is a GitHub Actions workflow, job and step?** A workflow is a YAML file of automation. It contains jobs (run on a runner machine), and jobs contain steps (single actions or commands).

**12. What does `needs:` do?** It makes a job wait for others. `deploy` needs `test` and `quality`, so it never runs if either fails.

**13. What is JUnit and what kinds of tests do we have?** JUnit 5 is the Java test framework. We have unit tests of the service with Mockito (fake repository) and integration tests of the REST API with MockMvc and a real H2 database.

**14. What is Mockito used for?** Creating fake dependencies, so the service is tested without a real database.

**15. What is Checkstyle?** A static analysis tool that checks code against rules (unused imports, naming, braces, line length). We configured it to fail the build on violations.

**16. How is code quality enforced?** `maven-checkstyle-plugin` with `failOnViolation=true` runs `checkstyle:check` in the Code Quality job; any violation fails the job.

**17. Which HTTP status codes does the API use and why?** 201 created, 200 OK, 204 deleted (no body), 400 invalid input, 404 not found, 409 conflict (duplicate email).

**18. What is validation and where is it done?** Rules such as `@NotBlank` and `@Email` on `StudentRequest`; `@Valid` in the controller triggers them; `GlobalExceptionHandler` converts failures to 400 JSON.

**19. Why use DTOs instead of exposing the entity?** To control exactly what JSON goes in and out and keep the database model private.

**20. Why H2?** It is in-memory and needs no install, ideal for demos and tests. Data disappears on restart, so production would use a real database.

**21. Is your project deployed?** No. We publish a verified JAR as a GitHub Actions artifact and smoke-test it on the runner. Real deployment would need a hosting platform and repository secrets.

**22. Why does the smoke test exist?** Passing tests do not prove the packaged JAR starts. The smoke test runs the real JAR and calls the API.

**23. What are GitHub secrets and why not commit passwords?** Encrypted values stored in repository settings and injected at runtime. Committed credentials leak through history.

**24. How would you show that the pipeline really protects quality?** Push an unused import or break a test: the matching job turns red and `deploy` does not run.

**25. What is a sprint retrospective?** A team meeting after the sprint: what went well, what did not, and what to improve.

# Definition of Done (DoD)

A user story is **Done** only when every box is true.

- [ ] Implementation is complete and matches the story.
- [ ] Code is committed with a clear message and pushed to GitHub (via branch/PR or directly to `main`).
- [ ] `mvn clean package` succeeds (Build job is green).
- [ ] All automated tests pass (Tests job is green), including tests for the story's acceptance criteria.
- [ ] `mvn checkstyle:check` reports 0 violations (Code Quality job is green).
- [ ] Every acceptance criterion of the story is demonstrably satisfied.
- [ ] Documentation (README / docs) is updated for the change.
- [ ] The verified JAR is published as an Actions artifact and the smoke test passed
      (applies to the release run on `main`).
- [ ] Another team member has reviewed the change.

**Measurable by:** a green GitHub Actions run on `main` whose four jobs all passed.

# Sprint Plan

**Sprint 1** | Duration: **1 week** | Team: [Team Member 1 Name], [Team Member 2 Name]

**Sprint goal:** deliver a working, tested Student Management API and a pipeline that automatically builds,
tests, quality-checks and publishes it.

**Planned stories:** US-01, US-02, US-03, US-04, US-05 (13 story points)

## Tasks

| Task | Story | Owner |
|---|---|---|
| Create Maven/Spring Boot project, GitHub repository, `.gitignore` | All | [Member 1] |
| Student entity, repository, DTOs with validation | US-01 | [Member 1] |
| Service with duplicate-email rule, create + list + get | US-01, US-02 | [Member 1] |
| Global exception handler (400/404/409) | US-01 | [Member 2] |
| Update and delete endpoints | US-03, US-04 | [Member 2] |
| Service unit tests (Mockito) | US-01..04 | [Member 2] |
| Controller integration tests (MockMvc) | US-01..04 | [Member 1] |
| Checkstyle configuration | US-05 | [Member 2] |
| GitHub Actions workflow + smoke-test script | US-05 | [Member 1] |
| README and docs, viva questions | All | [Member 2] |
| Record the 90-second demo video | US-05 | Both |

## Suggested schedule

| Day | Focus |
|---|---|
| 1 | Planning, project setup, repository, first push |
| 2-3 | API features and validation |
| 4 | Tests and Checkstyle |
| 5 | Pipeline, fix failures, documentation |
| 6 | Dry-run the demo, then record video |
| 7 | Sprint review and retrospective |

## Deliverables
Working API, passing test suite, Checkstyle config, green pipeline run, downloadable JAR artifact,
documentation, demo video.

## Sprint review
Demo the live pipeline run and the API to the instructor/peers. Walk through each story against its
acceptance criteria and the Definition of Done.

## Sprint retrospective (fill in after the sprint)
- What went well?
- What was difficult (for example failing builds, merge conflicts)?
- One improvement for the next sprint.

# 90-Second Demo Script (2 members)

Record **your own voices** (no AI narration). Use screen recording (for example OBS or Win+G). Do a dry run first.
Before recording: have a green run in the Actions tab, IntelliJ open on the project, and the docs folder visible.
Total target: about 90 seconds (limit: 2 minutes).

| Time | Speaker | Screen | Say (adapt to your own words) | Evidence visible |
|---|---|---|---|---|
| 0:00-0:10 | Member 1 | GitHub repo home page | "We are [names]. Our project is a Student Management REST API built with Spring Boot and Java 21, with an automated CI/CD pipeline on GitHub Actions." | repo name, file list |
| 0:10-0:30 | Member 1 | `docs/user-stories.md`, then `sprint-plan.md` and `definition-of-done.md` | "We wrote five user stories, for example adding a student. Sprint 1 lasts one week, tasks are split between the two of us. Our Definition of Done requires a green build, passing tests and Checkstyle, and a published artifact. The Add Student story has acceptance criteria for valid input, missing fields, invalid email and duplicate email." | story IDs, task table, DoD checklist |
| 0:30-0:45 | Member 2 | IntelliJ: `ci-cd.yml`, then the terminal | "The workflow triggers on every push to main. It has four jobs: Build, Tests, Code Quality with Checkstyle, and Publish." | the workflow file, `needs:` lines |
| 0:45-1:10 | Member 2 | Terminal: `git commit` and `git push`, then the Actions tab with the run in progress | "Now we push a change. GitHub Actions starts automatically. Build compiles and packages the JAR. Tests run our JUnit tests. Checkstyle checks code quality." | commit, push, run starting, jobs turning green |
| 1:10-1:25 | Member 1 | Finished run: green graph, open the Tests log (test count) and the Artifacts section | "All jobs passed, so the verified JAR was started in a smoke test and published as a downloadable artifact. This is artifact publishing, not hosting on a live server." | green checks, "Tests run: N, Failures: 0", artifact name |
| 1:25-1:30 | Both | Run summary | "So every change is built, tested, quality-checked and packaged automatically. Thank you." | final green run |

**Optional failure shot (only if time allows, otherwise skip):** push an unused import so Code Quality turns red,
then explain it blocks publishing. Rehearse so the whole video stays under 2:00.

Recording checklist: mic test, close notifications, zoom the browser to 125%, hide tokens/emails you don't want shown.

# User Stories

Format: **As a [user], I want [feature], so that [benefit].**

| ID | Title | Priority | Story points |
|---|---|---|---|
| US-01 | Add a student | High | 3 |
| US-02 | View students | High | 2 |
| US-03 | Update a student | Medium | 2 |
| US-04 | Delete a student | Medium | 1 |
| US-05 | Automated quality pipeline | High | 5 |

## US-01: Add a student (High)
As an **administrator**, I want to add a student with name, email and course, so that the student is recorded.
- Valid data creates the student and returns 201 with the new id.
- Missing or invalid fields are rejected with 400 and field-level messages.
- A duplicate email is rejected with 409.
- Full criteria: [acceptance-criteria.md](acceptance-criteria.md).
- **Outcome:** new students are stored with unique emails.

## US-02: View students (High)
As an **administrator**, I want to list all students and open one by id, so that I can see current records.
- `GET /api/students` returns 200 and a list (empty list if none).
- `GET /api/students/{id}` returns 200 with the student, or 404 if missing.
- **Outcome:** records can be looked up reliably.

## US-03: Update a student (Medium)
As an **administrator**, I want to edit a student's details, so that mistakes and changes are corrected.
- `PUT /api/students/{id}` with valid data returns 200 with updated values.
- Unknown id returns 404; invalid data returns 400.
- Using another student's email returns 409; keeping one's own email is allowed.
- **Outcome:** records stay accurate.

## US-04: Delete a student (Medium)
As an **administrator**, I want to delete a student, so that records that are no longer needed are removed.
- `DELETE /api/students/{id}` returns 204 and the student can no longer be fetched (404).
- Unknown id returns 404.
- **Outcome:** obsolete records are removed.

## US-05: Automated quality pipeline (High)
As a **developer**, I want every push to be built, tested and style-checked automatically, so that broken
code is caught early and only verified code is published.
- A push to `main` triggers build, tests, Checkstyle and publish jobs in order.
- A failing test or Checkstyle violation turns the run red and stops publishing.
- A passing run publishes the JAR as a downloadable artifact.
- **Outcome:** a reliable, repeatable path from commit to release-ready JAR.

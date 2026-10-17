# Agile Plan: Student Management System

This is the overview. Details are in: [user-stories.md](user-stories.md), [sprint-plan.md](sprint-plan.md),
[definition-of-done.md](definition-of-done.md), [acceptance-criteria.md](acceptance-criteria.md).

**Team:** [Team Member 1 Name], [Team Member 2 Name]
**Product goal:** a small REST API where an administrator can manage student records, delivered through an
automated pipeline so every change is built, tested and quality-checked before it is published.

## Agile concepts in plain words

Think of building a house room by room instead of all at once, showing the owner each finished room.

| Concept | Meaning | Analogy |
|---|---|---|
| **Agile** | Build software in short cycles, deliver working pieces, adapt to feedback | Cooking a meal course by course and tasting as you go |
| **User story** | One feature written from the user's view: "As a ..., I want ..., so that ..." | A single order slip |
| **Sprint planning** | Choosing which stories to finish in a short fixed period and splitting them into tasks | Deciding this week's meals and who shops/cooks |
| **Acceptance criteria** | Testable conditions that decide if one story works | The customer's checklist for one dish |
| **Definition of Done (DoD)** | One shared checklist every story must pass to count as finished | Kitchen rule: cooked, tasted, plated, cleaned |
| **Continuous Integration (CI)** | Every push is automatically built and tested | A guard checking each new brick |
| **Continuous Delivery** | Every passing change is *ready* to release, release is a manual decision | A packed parcel waiting for you to press "send" |
| **Continuous Deployment** | Every passing change is released to users automatically | The parcel is sent automatically |

**Where this project sits:** CI is fully implemented (build, test, Checkstyle). The final stage publishes a
verified JAR (Continuous Delivery style: ready to deploy). It does not auto-deploy to a live server, so it is
not Continuous Deployment.

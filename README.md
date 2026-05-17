# University System

A console-based University System built with Java, implementing core Object-Oriented Programming principles.

## Description

This system simulates a real university intranet where students, teachers, managers, administrators, and researchers interact according to their roles and permissions. The system supports secure authentication, course management, grade tracking, research paper management, and full data persistence.

## Project Structure

```
src/university/
├── model/          → User, Student, Teacher, Manager, Admin, Course, Mark, Lesson
│   └── research/   → Researcher, ResearchPaper, ResearchProject, ResearcherDecorator
├── patterns/       → Database, NotificationService, UserFactory
├── exceptions/     → CreditLimitException, FailLimitException, LowHIndexException
└── enums/          → Department, TeacherTitle, ManagerType, LessonType, ProjectStatus
```

## Features

- Secure authentication via SHA-256 password hashing
- Course registration with credit limit enforcement
- Grade management with attestation validation (ATT1 + ATT2 ≤ 60, Final ≤ 40)
- Teacher permission validation — can only grade own students and courses
- Student can only register for courses that exist in the system
- Research paper tracking, h-index calculation, sorting by citations/pages/date
- Top cited researchers by school and by year
- Academic performance report generation
- Data persistence via Java serialization

## Design Patterns

| Pattern | Class |
|---|---|
| Singleton | Database |
| Observer | NotificationService |
| Factory | UserFactory |
| Decorator | ResearcherDecorator |

## Custom Exceptions

| Exception | When thrown |
|---|---|
| `CreditLimitException` | Student exceeds credit limit |
| `FailLimitException` | Student fails more than 3 courses |
| `LowHIndexException` | Supervisor h-index is below 3 |
| `NotAResearcherException` | Non-researcher tries to join a project |

## Technologies

- Java 17+
- IntelliJ IDEA
- StarUML (UML diagrams)
- Javadoc (documentation)

## Team

- **Kozhabergenov Nurzhan** (Team Lead) — 24B050485
- **Yelmuratova Madina** — 22B030343
- **Kainazar Nurdaulet** — 21B030682


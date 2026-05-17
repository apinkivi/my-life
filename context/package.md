# Package fi.apinkivi.life.context

# My Life Bounded Contexts

- [Task](task/package.md)
- [Calendar](calendar/package.md)

# Bounded Context structure

- `package.md` Specification
  - _Mermaid_ charts describing Domain Model
- `interface` Contract API project:
  - Application Service Intarfaces
  - Data Transfer Objects
- `service` Application layer project:
  - Application Service Implementation
- `core` Domain model layer project:
  - Aggregate root
  - Entities
  - Value Objects
  - Domain Service Interfaces
- `provider` Infrastructure layer project:
  - Domain Service Implementation
  - Libraries

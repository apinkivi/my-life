# My life

The goal is to build a personal AI assistant to help everyday life using Domain-Driven Design.

## Structure

- `app` Applications
  - `firebase` Firebase project and Single-Page Application
    - `functions` Cloud Functions project
- `context` [Bounded Contexts](context/package.md)
  - `task` [Task Management](context/task/package.md)
  - `calendar` [Calendar Management](context/calendar/package.md)
- `lib` Library projects

## Guidlines

- Good integration with Google and Microsoft products.
- Prefer Firebase environment.
- Prefer Kotlin where language doesn't matter.

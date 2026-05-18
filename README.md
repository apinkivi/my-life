# My life

This public project's goal is to build a personal AI assistant to help everyday life using Domain-Driven Design.

## Structure

- `app` Applications
  - `firebase` Firebase project and Single-Page Application
    - `functions` Cloud Functions project
      - [x] Syncronize Tasks using Google Tasks API
  - [ ] Chrome extension to manage recurring tasks on Google Tasks
- `context` [Bounded Contexts](context/package.md)
  - [x] `task` [Task Management](context/task/package.md)
  - [ ] `calendar` [Calendar Management](context/calendar/package.md)
- `lib` Library projects

## Guidlines

- Good integration with Google and Microsoft products.
- Prefer Firebase environment.
- Prefer Kotlin where language doesn't matter.

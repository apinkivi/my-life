import type { FieldValue } from "firebase-admin/firestore";

/**
 * Firestore document for a task list.
 * Path: users/{uid}/taskLists/{listId}
 *
 * @see context/task/package.md — TaskList aggregate
 */
export interface TaskListDocument {
  /** Display name of the task list. Identifier in the domain model. */
  name: string;
  /** Google Tasks API list ID. */
  googleId: string;
  /** ISO 8601 timestamp of last update in Google Tasks. */
  updated: string | null;
  /** Server timestamp of when this document was last synced. */
  syncedAt: FieldValue;
}

/**
 * Firestore document for a task.
 * Path: users/{uid}/taskLists/{listId}/tasks/{taskId}
 *
 * Aligned with the domain model in context/task/package.md.
 * Fields map to the Task interface properties:
 *   title → title
 *   details → details (notes)
 *   important → important (starred — not available in Tasks API v1)
 *   dueDate → dueDate
 *   created → created (ChangeHistory)
 *   modified → modified (ChangeHistory)
 */
export interface TaskDocument {
  /** Google Tasks API task ID. */
  googleId: string;
  /** Task title. Maps to domain `Task.title`. */
  title: string;
  /** Task notes/details. Maps to domain `Task.details`. */
  details: string | null;
  /** Whether the task is starred/important. Maps to domain `Task.important`. */
  important: boolean;
  /** Due date as RFC 3339 date string. Maps to domain `Task.dueDate`. */
  dueDate: string | null;
  /** Google Tasks status: 'needsAction' or 'completed'. */
  status: string;
  /** Completion timestamp as RFC 3339 string. */
  completed: string | null;
  /** Reference to the parent task list ID. Maps to domain `Task.list`. */
  list: string;
  /** Parent task ID for subtasks. */
  parent: string | null;
  /** Position within the list for ordering. */
  position: string | null;
  /** Creation/update time. Maps to domain `ChangeHistory.created`. */
  created: string | null;
  /** Last modified time. Maps to domain `ChangeHistory.modified`. */
  modified: string | null;
  /** Whether the task has been deleted. */
  deleted: boolean;
  /** Whether the task is hidden. */
  hidden: boolean;
  /** Server timestamp of when this document was last synced. */
  syncedAt: FieldValue;
}

import { tasks_v1 } from "@googleapis/tasks";
import { OAuth2Client } from "google-auth-library";
import { getFirestore, FieldValue } from "firebase-admin/firestore";
import { initializeApp, getApps } from "firebase-admin/app";
import type { TaskListDocument, TaskDocument } from "./types";

// Initialize Firebase Admin only once.
if (getApps().length === 0) {
  initializeApp();
}

const db = getFirestore();

/**
 * Fetches all task lists and their tasks from Google Tasks API
 * and saves them to Firestore under `users/{uid}/taskLists/{listId}/tasks/{taskId}`.
 */
export async function syncGoogleTasks(
  uid: string,
  accessToken: string
): Promise<{ taskLists: number; tasks: number }> {
  const auth = new OAuth2Client();
  auth.setCredentials({ access_token: accessToken });

  const tasksApi = new tasks_v1.Tasks({ auth });

  // 1. Fetch all task lists.
  const taskLists = await fetchAllTaskLists(tasksApi);

  let totalTasks = 0;

  // 2. For each list, fetch all tasks and write to Firestore.
  for (const list of taskLists) {
    if (!list.id || !list.title) continue;

    const listDoc = toTaskListDocument(list);
    const listRef = db
      .collection("users")
      .doc(uid)
      .collection("taskLists")
      .doc(list.id);

    await listRef.set(listDoc, { merge: true });

    const tasks = await fetchAllTasks(tasksApi, list.id);

    // Batch write tasks (max 500 per batch in Firestore).
    const batches = chunk(tasks, 450);
    for (const batch of batches) {
      const writeBatch = db.batch();
      for (const task of batch) {
        if (!task.id) continue;
        const taskDoc = toTaskDocument(task, list.id);
        const taskRef = listRef.collection("tasks").doc(task.id);
        writeBatch.set(taskRef, taskDoc, { merge: true });
        totalTasks++;
      }
      await writeBatch.commit();
    }
  }

  // 3. Record sync metadata.
  await db.collection("users").doc(uid).set(
    {
      lastTaskSync: FieldValue.serverTimestamp(),
      taskSyncStats: {
        taskLists: taskLists.length,
        tasks: totalTasks,
      },
    },
    { merge: true }
  );

  return { taskLists: taskLists.length, tasks: totalTasks };
}

/**
 * Fetches all task lists, handling pagination.
 */
async function fetchAllTaskLists(
  tasksApi: tasks_v1.Tasks
): Promise<tasks_v1.Schema$TaskList[]> {
  const allLists: tasks_v1.Schema$TaskList[] = [];
  let pageToken: string | undefined;

  do {
    const response = await tasksApi.tasklists.list({
      maxResults: 100,
      pageToken,
    });
    if (response.data.items) {
      allLists.push(...response.data.items);
    }
    pageToken = response.data.nextPageToken ?? undefined;
  } while (pageToken);

  return allLists;
}

/**
 * Fetches all tasks in a list, handling pagination.
 * Includes completed and hidden tasks for full backup.
 */
async function fetchAllTasks(
  tasksApi: tasks_v1.Tasks,
  taskListId: string
): Promise<tasks_v1.Schema$Task[]> {
  const allTasks: tasks_v1.Schema$Task[] = [];
  let pageToken: string | undefined;

  do {
    const response = await tasksApi.tasks.list({
      tasklist: taskListId,
      maxResults: 100,
      showCompleted: true,
      showHidden: true,
      pageToken,
    });
    if (response.data.items) {
      allTasks.push(...response.data.items);
    }
    pageToken = response.data.nextPageToken ?? undefined;
  } while (pageToken);

  return allTasks;
}

/**
 * Maps a Google Tasks API TaskList to our Firestore document shape.
 */
function toTaskListDocument(
  list: tasks_v1.Schema$TaskList
): TaskListDocument {
  return {
    name: list.title ?? "",
    googleId: list.id ?? "",
    updated: list.updated ?? null,
    syncedAt: FieldValue.serverTimestamp(),
  };
}

/**
 * Maps a Google Tasks API Task to our Firestore document shape.
 * Aligned with the domain model in context/task/package.md.
 */
function toTaskDocument(
  task: tasks_v1.Schema$Task,
  listId: string
): TaskDocument {
  return {
    googleId: task.id ?? "",
    title: task.title ?? "",
    details: task.notes ?? null,
    important: false, // Google Tasks starred status not available in API v1.
    dueDate: task.due ?? null,
    status: task.status ?? "needsAction",
    completed: task.completed ?? null,
    list: listId,
    parent: task.parent ?? null,
    position: task.position ?? null,
    created: task.updated ?? null, // Tasks API v1 doesn't expose creation time.
    modified: task.updated ?? null,
    deleted: task.deleted ?? false,
    hidden: task.hidden ?? false,
    syncedAt: FieldValue.serverTimestamp(),
  };
}

/**
 * Splits an array into chunks of the given size.
 */
function chunk<T>(array: T[], size: number): T[][] {
  const chunks: T[][] = [];
  for (let i = 0; i < array.length; i += size) {
    chunks.push(array.slice(i, i + size));
  }
  return chunks;
}

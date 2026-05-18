import { onCall, HttpsError } from "firebase-functions/v2/https";
import { syncGoogleTasks } from "./tasks/sync-google-tasks";

/**
 * Callable Cloud Function: syncTasks
 *
 * Fetches all Google Tasks for the authenticated user and saves them to Firestore.
 * Requires the user to be authenticated via Firebase Auth with a Google provider
 * and have granted the `tasks.readonly` OAuth scope.
 */
export const syncTasks = onCall(
  { region: "europe-west1" },
  async (request) => {
    if (!request.auth) {
      throw new HttpsError(
        "unauthenticated",
        "User must be authenticated."
      );
    }

    const uid = request.auth.uid;
    const googleAccessToken = request.data?.accessToken as string | undefined;

    if (!googleAccessToken) {
      throw new HttpsError(
        "invalid-argument",
        "Google OAuth access token is required. Pass it as data.accessToken."
      );
    }

    try {
      const result = await syncGoogleTasks(uid, googleAccessToken);
      return result;
    } catch (error) {
      console.error("syncTasks failed:", error);
      throw new HttpsError(
        "internal",
        `Failed to sync tasks: ${error instanceof Error ? error.message : String(error)}`
      );
    }
  }
);

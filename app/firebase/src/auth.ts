import {
  signInWithPopup,
  GoogleAuthProvider,
  signOut,
  type User,
  type OAuthCredential,
} from "firebase/auth";
import { httpsCallable } from "firebase/functions";
import { auth, functions } from "./firebase";

// Request Google Tasks read scope during sign-in.
const googleProvider = new GoogleAuthProvider();
googleProvider.addScope("https://www.googleapis.com/auth/tasks.readonly");

const syncTasksCallable = httpsCallable<
  { accessToken: string },
  { taskLists: number; tasks: number }
>(functions, "syncTasks");

let currentAccessToken: string | null = null;

/**
 * Signs in the user with Google and stores the OAuth access token
 * needed for Google Tasks API access.
 */
export async function signInWithGoogle(): Promise<User> {
  const result = await signInWithPopup(auth, googleProvider);
  const credential: OAuthCredential | null =
    GoogleAuthProvider.credentialFromResult(result);
  currentAccessToken = credential?.accessToken ?? null;
  return result.user;
}

/**
 * Signs out the current user.
 */
export async function signOutUser(): Promise<void> {
  currentAccessToken = null;
  await signOut(auth);
}

/**
 * Calls the syncTasks Cloud Function to fetch and store all Google Tasks.
 */
export async function syncTasks(): Promise<{
  taskLists: number;
  tasks: number;
}> {
  if (!currentAccessToken) {
    throw new Error(
      "No Google access token available. Please sign in again."
    );
  }

  const result = await syncTasksCallable({
    accessToken: currentAccessToken,
  });
  return result.data;
}

/**
 * Returns the currently authenticated user, or null.
 */
export function getCurrentUser(): User | null {
  return auth.currentUser;
}

/**
 * Subscribe to auth state changes.
 */
export function onAuthStateChanged(
  callback: (user: User | null) => void
): () => void {
  return auth.onAuthStateChanged(callback);
}
